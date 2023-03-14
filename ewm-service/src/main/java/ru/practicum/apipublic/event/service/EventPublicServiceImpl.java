package ru.practicum.apipublic.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.client.Client;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.mapper.EventMapper;
import ru.practicum.model.event.model.Event;
import ru.practicum.model.event.model.QEvent;
import ru.practicum.model.event.repository.EventRepository;
import ru.practicum.model.event.status_event.StatusEvent;
import ru.practicum.model.participation.repository.RequestRepository;


import javax.servlet.http.HttpServletRequest;
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

    private final StatsClient statsClient;

    private final Client client;

    public EventPublicServiceImpl(EventRepository eventRepository,
                                  EventMapper eventMapper,
                                  RequestRepository requestRepository,
                                  StatsClient statsClient,
                                  Client client) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.requestRepository = requestRepository;
        this.statsClient = statsClient;
        this.client = client;
    }

    @Override
    public EventFullDto getEventByEventId(Long eventId, HttpServletRequest httpServletRequest) {
        Event event = getValidEvent(eventId);
        if (!event.getState().equals(StatusEvent.PUBLISHED)) {
            throw new ConflictException("STATUS BAD");
        }
        Long confirmed = requestRepository.countConfirmedByEventId(event.getId());
        log.info("Получено событие {}", event);
        statsClient.createHit(httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event, confirmed);
        return client.setViewsToEventFullDto(eventFullDto);
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
                                                          Integer size,
                                                          HttpServletRequest httpServletRequest) {
        statsClient.createHit(httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
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
        return eventShortDtos;
    }

    private Event getValidEvent(Long eventId) {
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
