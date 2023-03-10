package ru.practicum.api_private.user_event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api_private.user_event.service.UserEventService;
import ru.practicum.model_package.event.dto.EventFullDto;
import ru.practicum.model_package.event.dto.EventShortDto;
import ru.practicum.model_package.event.dto.NewEventDto;
import ru.practicum.model_package.event.dto.UpdateEventUserRequestDto;
import ru.practicum.model_package.participation_request.dto.ParticipationRequestDto;
import ru.practicum.model_package.participation_request.model.EventRequestStatusUpdateRequest;
import ru.practicum.model_package.participation_request.model.EventRequestStatusUpdateResult;

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
                                    @Valid @RequestBody UpdateEventUserRequestDto updateEventUserRequestDto) {
        log.info("PATCH privateUserEvent обновление события пользователя с id={}, eventId={}", userId, eventId);
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
    public EventRequestStatusUpdateResult updateStatusRequest(@NotNull @PathVariable Long userId,
                                                              @NotNull @PathVariable Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest ids) {
        log.info("PATCH privateUserEvent запрос изменения статуса заявок участия в событии" +
                "пользователь={}, событие={}, список заявок={}", userId, eventId, ids);
        return userEventService.updateStatusRequest(userId, eventId, ids);
    }
}