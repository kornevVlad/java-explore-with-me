package ru.practicum.api_public.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model_package.event.dto.EventFullDto;
import ru.practicum.model_package.event.dto.EventShortDto;
import ru.practicum.model_package.event.mapper.EventMapper;
import ru.practicum.model_package.event.model.Event;
import ru.practicum.model_package.event.model.QEvent;
import ru.practicum.model_package.event.repository.EventRepository;
import ru.practicum.model_package.event.status_event.StatusEvent;
import ru.practicum.model_package.participation_request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EventPublicServiceImpl implements EventPublicService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final RequestRepository requestRepository;

    public EventPublicServiceImpl(EventRepository eventRepository,
                                  EventMapper eventMapper,
                                  RequestRepository requestRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.requestRepository = requestRepository;
    }

    @Override
    public EventFullDto getEventByEventId(Long eventId) {
        Event event = validEvent(eventId);
        if (!event.getState().equals(StatusEvent.PUBLISHED)) { //ВНЕСТИ ПРАВКИ ПО СТАТУСАМ !!!!!!!!!!!!
            return null;
        }
        Long confirmed = requestRepository.countConfirmedByEventId(event.getId());
        log.info("Получено событие {}", event);
        return eventMapper.toEventFullDto(event, confirmed);
    }

    @Override
    public List<EventShortDto> getAllEventsByStatusPublic(String text,
                                                          List<Long> categories,
                                                          Boolean paid,
                                                          String rangeStart,
                                                          String rangeEnd,
                                                          Boolean onlyAvailable,
                                                          String sort,
                                                          Integer from,
                                                          Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        LocalDateTime start;
        LocalDateTime end;
        List<Event> events;
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        QEvent qEvent = QEvent.event;
        BooleanExpression booleanExpression = qEvent.state.eq(StatusEvent.PUBLISHED);
        if (text != null) {
            booleanExpression = booleanExpression.and(qEvent.annotation.containsIgnoreCase(text)
                            .or(qEvent.description.containsIgnoreCase(text)));
        }
        if (categories != null) {
            booleanExpression = booleanExpression.and(qEvent.category.id.in(categories));
        }
        if (paid != null) {
            booleanExpression = booleanExpression.and(qEvent.paid.eq(paid));
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

        //проверка лимита участников в событии
        for (Event event : events) {
            Long count = requestRepository.countConfirmedByEventId(event.getId());
            if (event.getParticipantLimit() > count) {
                eventShortDtos.add(eventMapper.toEventShortDto(event, count));
            }
        }
        // ДОБАВИТЬ СОРТИРОВКУ ПО ДЛЯ ВЫВОДА!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11

       // RestTemplate restTemplate = new RestTemplate();
       // restTemplate.post

        return eventShortDtos;
    }

    private Event validEvent(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException("NOT FOUND EVENT_ID " + eventId);
        }
        return event.get();
    }


    private LocalDateTime generateDataTimeToLocalDataTime(String dataTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dataTime, formatter);
    }
}
