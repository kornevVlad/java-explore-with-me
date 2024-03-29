package ru.practicum.apiprivate.participation.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.modelpackage.event.model.Event;
import ru.practicum.modelpackage.event.repository.EventRepository;
import ru.practicum.modelpackage.event.status_event.StatusEvent;
import ru.practicum.modelpackage.participation.dto.ParticipationRequestDto;
import ru.practicum.modelpackage.participation.mapper.RequestMapper;
import ru.practicum.modelpackage.participation.model.ParticipationRequest;
import ru.practicum.modelpackage.participation.repository.RequestRepository;
import ru.practicum.modelpackage.participation.status_request.StatusRequest;
import ru.practicum.modelpackage.user.model.User;
import ru.practicum.modelpackage.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    public ParticipationRequestServiceImpl(UserRepository userRepository,
                                           EventRepository eventRepository,
                                           RequestRepository requestRepository,
                                           RequestMapper requestMapper) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
    }

    @Override
    public ParticipationRequestDto addRequestByUser(Long userId, Long eventId) {
        Event event = getEvent(eventId);
        User user = getUser(userId);
        StatusEvent status = StatusEvent.PUBLISHED;
        validUserAndEvent(user, event, status);
        ParticipationRequest participationRequest;
        if (!event.getRequestModeration()) {
            participationRequest = requestMapper.toParticipationRequest(user, event, StatusRequest.CONFIRMED);
        } else {
            participationRequest = requestMapper.toParticipationRequest(user, event, StatusRequest.PENDING);
        }
        log.info("Создана заявка на участие в событии = {}", participationRequest);
        try {
            return requestMapper.toParticipationRequestDto(requestRepository.save(participationRequest));
        } catch (RuntimeException e) {
            throw new ConflictException("could not execute statement; SQL [n/a]; constraint [uq_request];" +
                    " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                    " could not execute statement");
        }
    }

    @Override
    public List<ParticipationRequestDto> getRequestByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
        List<ParticipationRequest> requests = requestRepository.findParticipationRequestByRequesterId(userId);
        List<ParticipationRequestDto> requestDto = new ArrayList<>();
        for (ParticipationRequest request : requests) {
            requestDto.add(requestMapper.toParticipationRequestDto(request));
        }
        log.info("Список запросов пользователя с id={}, список: {}", userId, requestDto);
        return requestDto;
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        getUser(userId);
        ParticipationRequest participationRequest = getRequestByUserId(userId, requestId);
        participationRequest.setStatus(StatusRequest.CANCELED);
        log.info("Статус запроса изменен ={}", participationRequest);
        return requestMapper.toParticipationRequestDto(requestRepository.save(participationRequest));
    }

    /**
     *Валидация пользователя
     */
    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new BadRequestException("Failed to convert value of type java.lang.String" +
                    " to required type long; nested exception is java.lang.NumberFormatException:" +
                    " For input string: ad");
        }
        return user.get();
    }

    /**
     *Валидация события
     */
    private Event getEvent(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        return event.get();
    }

    /**
     * Валидация запроса
     */
    private ParticipationRequest getRequestByUserId(Long userId, Long requestId) {
        Optional<ParticipationRequest> participationRequest = requestRepository.findById(requestId);
        if (participationRequest.isEmpty()) {
            throw new NotFoundException("Request with id=" + requestId + " was not found");
        }
        //Проверка пользователя и его запроса
        ParticipationRequest p = participationRequest.get();
        User user = p.getRequester();
        if (!user.getId().equals(userId)) {
            throw new BadRequestException("Bad request");
        }
        return participationRequest.get();
    }

    /**
     * Валидация 409 ошибка
     */
    private void validUserAndEvent(User user, Event event, StatusEvent status) {
        //Пользователь не может создать запрос собственного события
        if (event.getInitiator().getId().equals(user.getId())) {
            log.error("Пользователь не может создать запрос собственного события");
            throw new ConflictException("USER CANNOT REQUEST OWN EVENT");
        }
        //Запрос отправляется толь на опубликованные события
        if (!event.getState().equals(status)) {
            log.error("Запрос отправляется толь на опубликованные события");
            throw new ConflictException("REQUEST NOT PUBLIC EVENTS");
        }
        //Лимит заявок не должен превышать лимит участников в событии
        if (requestRepository.countByEventAndStatus(event, StatusRequest.CONFIRMED).equals(event.getParticipantLimit())) {
            log.error("Лимит заявок не должен превышать лимит участников в событии");
            throw new ConflictException("LIMIT REQUEST IS OVER");
        }
    }
}