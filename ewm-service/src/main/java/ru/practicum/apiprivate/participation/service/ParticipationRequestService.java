package ru.practicum.apiprivate.participation.service;

import ru.practicum.model.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto addRequestByUser(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestByUserId(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
