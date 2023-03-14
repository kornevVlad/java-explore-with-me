package ru.practicum.apiadmin.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.categories.model.Category;
import ru.practicum.model.categories.repository.CategoryRepository;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.model.event.mapper.EventMapper;
import ru.practicum.model.event.model.Event;

import ru.practicum.model.event.model.QEvent;
import ru.practicum.model.event.repository.EventRepository;
import ru.practicum.model.event.status_event.AdminStatusEvent;
import ru.practicum.model.event.status_event.StatusEvent;
import ru.practicum.model.participation.repository.RequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EventAdminServiceImpl implements EventAdminService {

    private final EventMapper eventMapper;

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final RequestRepository requestRepository;

    public EventAdminServiceImpl(EventMapper eventMapper,
                                 EventRepository eventRepository,
                                 CategoryRepository categoryRepository,
                                 RequestRepository requestRepository) {
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        Event event = getValidEvent(eventId);
        if (updateEventAdminRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequestDto.getAnnotation());
        }
        if (updateEventAdminRequestDto.getCategory() != null) {
            Category category = getValidCategory(updateEventAdminRequestDto.getCategory());
            event.setCategory(category);
        }
        if (updateEventAdminRequestDto.getDescription() != null) {
            event.setDescription(updateEventAdminRequestDto.getDescription());
        }
        if (updateEventAdminRequestDto.getEventDate() != null) {
            LocalDateTime dateTime = generateDataTimeToLocalDataTime(updateEventAdminRequestDto.getEventDate());
            if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictException("Field: eventDate. Error: должно содержать дату," +
                        " которая еще не наступила. Value:" + dateTime);
            }
            event.setEventDate(dateTime);
        }
        if (updateEventAdminRequestDto.getLocation() != null) {
            event.setLat(updateEventAdminRequestDto.getLocation().getLat());
            event.setLon(updateEventAdminRequestDto.getLocation().getLon());
        }
        if (updateEventAdminRequestDto.getPaid() != null) {
            event.setPaid(updateEventAdminRequestDto.getPaid());
        }
        if (updateEventAdminRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequestDto.getParticipantLimit());
        }
        if (updateEventAdminRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequestDto.getRequestModeration());
        }
        if (updateEventAdminRequestDto.getStateAction() != null) {
            //опубликованное событие не изменяется администратором
            if (event.getState().equals(StatusEvent.PUBLISHED)) {
                throw new ConflictException("Cannot publish the event because it's not in the right state: PUBLISHED");
            }
            // 409 при изменении завершенного события
            if (event.getState().equals(StatusEvent.CANCELED)) {
                throw new ConflictException("Cannot publish the event because it's not in the right state: PUBLISHED");
            }
            //изменить статус можно только у события в статусе ожидания
            if (event.getState().equals(StatusEvent.PENDING)) {
                event.setState(generateState(updateEventAdminRequestDto.getStateAction()));
                if (event.getState().equals(StatusEvent.PUBLISHED)) {
                    event.setPublishedOn(LocalDateTime.now());
                }
            }
        }
        if (updateEventAdminRequestDto.getTitle() != null) {
            event.setTitle(updateEventAdminRequestDto.getTitle());
        }
        log.info("Событие обновлено администратором {}", event);
        Long confirmed = requestRepository.countConfirmedByEventId(event.getId());
        return eventMapper.toEventFullDto(eventRepository.save(event),confirmed);
    }

    @Override
    public List<EventFullDto> getAllEventsByFilter(List<Long> users,
                                                   List<StatusEvent> states,
                                                   List<Long> categories,
                                                   String rangeStart,
                                                   String rangeEnd,
                                                   Integer from,
                                                   Integer size) {
        LocalDateTime start;
        LocalDateTime end;
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = new ArrayList<>();
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        QEvent qEvent = QEvent.event;
        BooleanExpression booleanExpression = qEvent.id.isNotNull();
        if (users != null) {
            booleanExpression = booleanExpression.and(qEvent.initiator.id.in(users));
        }
        if (states != null) {
            booleanExpression = booleanExpression.and(qEvent.state.in(states));
        }
        if (categories != null) {
            booleanExpression = booleanExpression.and(qEvent.category.id.in(categories));
        }
        if (rangeStart != null) {
            start = generateDataTimeToLocalDataTime(rangeStart);
            booleanExpression = booleanExpression.and(qEvent.eventDate.goe(start));
        }
        if (rangeEnd != null) {
             end = generateDataTimeToLocalDataTime(rangeEnd);
            booleanExpression = booleanExpression.and(qEvent.eventDate.loe(end));
        }
        events = eventRepository.findAll(booleanExpression, pageable).getContent();
        System.out.println(events);
        for (Event event : events) {
            Long count = requestRepository.countConfirmedByEventId(event.getId());
            eventFullDtos.add(eventMapper.toEventFullDto(event, count));
        }
        return eventFullDtos;
    }

    private Event getValidEvent(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException("NOT FOUND");
        }
        return event.get();
    }

    private Category getValidCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new NotFoundException("Not found");
        }
        return category.get();
    }

    private LocalDateTime generateDataTimeToLocalDataTime(String dataTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dataTime, formatter);
    }

    private StatusEvent generateState(AdminStatusEvent state) {
        StatusEvent st = null;
        if (state.equals(AdminStatusEvent.PUBLISH_EVENT)) {
            st = StatusEvent.PUBLISHED;
        }
        if (state.equals(AdminStatusEvent.REJECT_EVENT)) {
            st = StatusEvent.CANCELED;
        }
        return st;
    }
}