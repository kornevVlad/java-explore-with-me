package ru.practicum.apiprivate.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.apiprivate.event.service.UserEventService;
import ru.practicum.modelpackage.event.dto.EventFullDto;
import ru.practicum.modelpackage.event.dto.EventShortDto;
import ru.practicum.modelpackage.event.dto.NewEventDto;
import ru.practicum.modelpackage.event.dto.UpdateEventUserRequestDto;
import ru.practicum.modelpackage.participation.dto.ParticipationRequestDto;
import ru.practicum.modelpackage.participation.model.EventRequestStatusUpdateRequest;
import ru.practicum.modelpackage.participation.model.EventRequestStatusUpdateResult;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/users/{userId}")
public class UserEventController {

    private final UserEventService userEventService;

    public UserEventController(UserEventService userEventService) {
        this.userEventService = userEventService;
    }


    /**
     * Создание события пользователем
     */
    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                   @Valid @RequestBody NewEventDto newEventDto) {
        log.info("POST privateUserEvent добавление события userId={}, event={}", userId, newEventDto);
        return userEventService.createEvent(userId, newEventDto);
    }

    /**
     * Получение списка событий пользователя
     */
    @GetMapping("/events")
    public List<EventShortDto> getAllEventsByUserId(@NotNull @PathVariable Long userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET privateUserEvent список событий пользователя с id={}", userId);
        return userEventService.getAllEventsByUserId(userId, from, size);
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     */
    @GetMapping("/events/{eventId}")
    public EventFullDto getEventByUserId(@NotNull @PathVariable Long userId,
                                         @NotNull @PathVariable Long eventId) {
        log.info("GET privateUserEvent получение полной информации события пользователя с id={}", userId);
        return userEventService.getEventByUserId(userId, eventId);
    }

    /**
     * Изменение события добавленного текущим пользователем
     */
    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@NotNull @PathVariable Long userId,
                                    @NotNull @PathVariable Long eventId,
                                    @RequestBody UpdateEventUserRequestDto updateEventUserRequestDto) {
        log.info("PATCH privateUserEvent обновление события пользователя с id={}, eventId={}", userId, eventId);
        log.info("PATCH update = {}", updateEventUserRequestDto);
        return userEventService.updateEventByUserId(userId, eventId, updateEventUserRequestDto);
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     */
    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsInEventByUserId(@NotNull @PathVariable Long userId,
                                                                    @NotNull @PathVariable Long eventId) {
        log.info("GET privateUserEvent список запросов " +
                "участия в событии пользователя ={}, событие ={}", userId, eventId);
        return userEventService.getRequestsInEventByUserId(userId, eventId);
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     */
    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequest(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("PATCH privateUserEvent запрос изменения статуса заявок участия в событии" +
                "пользователь={}, событие={}, список заявок={}", userId, eventId, request);
        return userEventService.updateStatusRequest(userId, eventId, request);
    }
}