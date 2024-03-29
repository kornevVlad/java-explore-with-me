package ru.practicum.apiprivate.event.service;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.client.Client;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.modelpackage.categories.model.Category;
import ru.practicum.modelpackage.categories.repository.CategoryRepository;
import ru.practicum.modelpackage.event.dto.EventFullDto;
import ru.practicum.modelpackage.event.dto.EventShortDto;
import ru.practicum.modelpackage.event.dto.NewEventDto;
import ru.practicum.modelpackage.event.dto.UpdateEventUserRequestDto;
import ru.practicum.modelpackage.event.mapper.EventMapper;
import ru.practicum.modelpackage.event.model.Event;
import ru.practicum.modelpackage.event.repository.EventRepository;
import ru.practicum.modelpackage.event.status_event.StatusEvent;
import ru.practicum.modelpackage.event.status_event.UserStatusEvent;
import ru.practicum.modelpackage.participation.dto.ParticipationRequestDto;
import ru.practicum.modelpackage.participation.mapper.RequestMapper;
import ru.practicum.modelpackage.participation.model.EventRequestStatusUpdateRequest;
import ru.practicum.modelpackage.participation.model.EventRequestStatusUpdateResult;
import ru.practicum.modelpackage.participation.model.ParticipationRequest;
import ru.practicum.modelpackage.participation.repository.RequestRepository;
import ru.practicum.modelpackage.participation.status_request.StatusRequest;
import ru.practicum.modelpackage.user.model.User;
import ru.practicum.modelpackage.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Data
public class UserEventServiceImpl implements UserEventService {


    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    private final EventMapper eventMapper;

    private final Client client;


    public UserEventServiceImpl(UserRepository userRepository,
                                CategoryRepository categoryRepository,
                                EventRepository eventRepository,
                                RequestRepository requestRepository,
                                RequestMapper requestMapper,
                                EventMapper eventMapper,
                                Client client) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
        this.eventMapper = eventMapper;
        this.client = client;
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        User user = getUser(userId);
        Category category = getCategory(newEventDto.getCategory());
        Event event = eventMapper.toEvent(newEventDto, user, category);
        if (newEventDto.getEventDate() != null
                &&  event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Field: eventDate. Error: должно содержать дату," +
                    " которая еще не наступила. Value:" + event.getEventDate().toString());
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        }
        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        Long confirmed = requestRepository.countConfirmedByEventId(event.getId());
        return eventMapper.toEventFullDto(eventRepository.save(event), confirmed);
    }

    @Override
    public List<EventShortDto> getAllEventsByUserId(Long userId, Integer from, Integer size) {
        getUser(userId);
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        List<EventShortDto> eventsShortDto = new ArrayList<>();
        for (Event event : events) {
            Long confirmed = requestRepository.countConfirmedByEventId(event.getId());
            eventsShortDto.add(eventMapper.toEventShortDto(event, confirmed));
        }
        log.info("Список событий пользователя с id={}, список ={}", userId, eventsShortDto);
        return client.setViewsToEventsShortDto(eventsShortDto);
    }

    @Override
    public EventFullDto getEventByUserId(Long userId, Long eventId) {
        Event event = getEvent(eventId);
        User user = getUser(userId);
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new BadRequestException("USER DID NOT FIND THE EVENT");
        }
        log.info("Получен  Event = {}", event);
        Long confirmed = requestRepository.countConfirmedByEventId(event.getId());
        return client.setViewsToEventFullDto(eventMapper.toEventFullDto(event, confirmed));
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUserId(Long userId, Long eventId,
                                            UpdateEventUserRequestDto updateEventUserRequestDto) {
        log.info("Обновление события: данные для обновления = {}", updateEventUserRequestDto);
        Event event = getEvent(eventId);
        getUser(userId);
        //Проверка подходящих статусов
        if (updateEventUserRequestDto.getStateAction() == null) {
            throw new ConflictException("CONFLICT STATUS NULL");
        }
        if (updateEventUserRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequestDto.getAnnotation());
        }
        if (updateEventUserRequestDto.getCategory() != null) {
            //добавить валидацию
            Category category = getCategory(updateEventUserRequestDto.getCategory());
            event.setCategory(category);
        }
        if (updateEventUserRequestDto.getDescription() != null) {
            event.setDescription(updateEventUserRequestDto.getDescription());
        }
        if (updateEventUserRequestDto.getEventDate() != null) {
            LocalDateTime dateTime = generateDataTimeToLocalDataTime(updateEventUserRequestDto.getEventDate());
            if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictException("Field: eventDate. Error: должно содержать дату," +
                        " которая еще не наступила. Value:" + dateTime);
            }
            event.setEventDate(dateTime);
        }
        if (updateEventUserRequestDto.getLocation() != null) {
            event.setLon(updateEventUserRequestDto.getLocation().getLon());
            event.setLat(updateEventUserRequestDto.getLocation().getLat());
        }
        if (updateEventUserRequestDto.getPaid() != null) {
            event.setPaid(updateEventUserRequestDto.getPaid());
        }
        if (updateEventUserRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequestDto.getParticipantLimit());
        }
        if (updateEventUserRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequestDto.getRequestModeration());
        }
        //Обновлять события только в статусе PENDING, CANCELED
        if (updateEventUserRequestDto.getStateAction() != null) {
            if (event.getState().equals(StatusEvent.PENDING)) {
                event.setState(generateStatus(updateEventUserRequestDto.getStateAction()));
            } else if (event.getState().equals(StatusEvent.CANCELED)) {
                event.setState(generateStatus(updateEventUserRequestDto.getStateAction()));
            } else {
                throw new ConflictException("Only pending or canceled events can be changed");
            }
        }
        if (updateEventUserRequestDto.getTitle() != null) {
            event.setTitle(updateEventUserRequestDto.getTitle());
        }
        log.info("Обновленный event = {}", event);
        Long confirmed = requestRepository.countConfirmedByEventId(event.getId());
        return eventMapper.toEventFullDto(eventRepository.save(event), confirmed);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsInEventByUserId(Long userId, Long eventId) {
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("Failed to convert value of type java.lang.String to required type int;" +
                    " nested exception is java.lang.NumberFormatException: For input string: ad");
        }
        List<ParticipationRequest> requests = requestRepository.findAllByEventId(eventId);
        List<ParticipationRequestDto> requestsDto = new ArrayList<>();
        for (ParticipationRequest request : requests) {
            requestsDto.add(requestMapper.toParticipationRequestDto(request));
        }
        return requestsDto;
    }

    @Override
    public EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId,
                                                             EventRequestStatusUpdateRequest ids) {

        Event event = getEvent(eventId);
        getUser(userId);
        EventRequestStatusUpdateResult statusResult = new EventRequestStatusUpdateResult();
        for (Long id : ids.getRequestIds()) {
            ParticipationRequest request = getRequest(id);
            if (requestRepository.countByEventAndStatus(event, StatusRequest.CONFIRMED) >= event.getParticipantLimit()) {
                throw new ConflictException("LIMIT REQUEST");
            }
            if (request.getStatus().equals(StatusRequest.PENDING)) {
                request.setStatus(ids.getStatus());
                if (request.getStatus().equals(StatusRequest.CONFIRMED)) {
                    statusResult.getConfirmedRequests().add(requestMapper.toParticipationRequestDto(request));
                }
                if (request.getStatus().equals(StatusRequest.REJECTED)) {
                    request.setStatus(StatusRequest.REJECTED);
                    statusResult.getRejectedRequests().add(requestMapper.toParticipationRequestDto(request));
                }
            } else if (request.getStatus().equals(StatusRequest.CONFIRMED)) {
                if (ids.getStatus().equals(StatusRequest.REJECTED)) {
                    throw new ConflictException("CONFLICT STATUS REQUEST");
                }
            }
            requestRepository.save(request);
        }
        return statusResult;
    }

    private StatusEvent generateStatus(UserStatusEvent status) { //Обновление статусов
        StatusEvent st = null;
        if (status.equals(UserStatusEvent.CANCEL_REVIEW)) {
            st = StatusEvent.CANCELED;
        }
        if (status.equals(UserStatusEvent.SEND_TO_REVIEW)) {
            st = StatusEvent.PENDING;
        }
        return st;
    }

    private Event getEvent(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException("Not Found event");
        }
        return event.get();
    }

    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Not Found user");
        }
        return user.get();
    }

    private ParticipationRequest getRequest(Long requestId) {
        Optional<ParticipationRequest> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new NotFoundException("Not Found request");
        }
        return request.get();
    }

    private Category getCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new NotFoundException("Not Found category");
        }
        return category.get();
    }

    private LocalDateTime generateDataTimeToLocalDataTime(String dataTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dataTime, formatter);
    }
}