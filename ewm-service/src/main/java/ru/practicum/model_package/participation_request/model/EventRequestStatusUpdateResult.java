package ru.practicum.model_package.participation_request.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model_package.participation_request.dto.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateResult {

    private List<ParticipationRequestDto> confirmedRequests = new ArrayList<>(); //Подтверждена

    private List<ParticipationRequestDto> rejectedRequests = new ArrayList<>(); //Отклонена
}