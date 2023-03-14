package ru.practicum.model.participation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.model.event.model.Event;
import ru.practicum.model.participation.dto.ParticipationRequestDto;
import ru.practicum.model.participation.status_request.StatusRequest;
import ru.practicum.model.user.model.User;
import ru.practicum.model.participation.model.ParticipationRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class RequestMapper {

    public ParticipationRequest toParticipationRequest(User user, Event event, StatusRequest status) {
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setCreated(LocalDateTime.now());
        participationRequest.setEvent(event);
        participationRequest.setRequester(user);
        participationRequest.setStatus(status);
        return participationRequest;
    }

    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(participationRequest.getId());
        participationRequestDto.setCreated(generateDataTimeToString(participationRequest.getCreated()));
        participationRequestDto.setEvent(participationRequest.getEvent().getId());
        participationRequestDto.setRequester(participationRequest.getRequester().getId());
        participationRequestDto.setStatus(participationRequest.getStatus());
        return participationRequestDto;
    }

   /* public List<ParticipationRequestDto> updateStatus(ParticipationRequestDto participationRequestDto) {
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        if (participationRequestDto.getStatus().equals("CONFIRMED")) {
            eventRequestStatusUpdateResult.getConfirmedRequests().add(participationRequestDto);
        } else {
            eventRequestStatusUpdateResult.getRejectedRequests().add(participationRequestDto);
        }
        return null;
    }*/

    private String generateDataTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}