package ru.practicum.api_private.user_event.service;

import ru.practicum.model_package.event.dto.EventFullDto;
import ru.practicum.model_package.event.dto.EventShortDto;
import ru.practicum.model_package.event.dto.NewEventDto;
import ru.practicum.model_package.event.dto.UpdateEventUserRequestDto;
import ru.practicum.model_package.participation_request.dto.ParticipationRequestDto;
import ru.practicum.model_package.participation_request.model.EventRequestStatusUpdateRequest;
import ru.practicum.model_package.participation_request.model.EventRequestStatusUpdateResult;

import java.util.List;

public interface UserEventService {

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getAllEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto getEventByUserId(Long userId, Long eventId);

    EventFullDto updateEventByUserId(Long userId, Long eventId, UpdateEventUserRequestDto updateEventUserRequestDto);

    List<ParticipationRequestDto> getRequestsInEventByUserId(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest ids);
}
