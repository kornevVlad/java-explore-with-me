package ru.practicum.api_private.participation_request_event_by_user.service;

import ru.practicum.model_package.participation_request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto addRequestByUser(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestByUserId(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
