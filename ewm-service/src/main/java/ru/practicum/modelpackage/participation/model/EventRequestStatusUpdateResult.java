package ru.practicum.modelpackage.participation.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.modelpackage.participation.dto.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateResult {

    private List<ParticipationRequestDto> confirmedRequests = new ArrayList<>(); //Подтверждена

    private List<ParticipationRequestDto> rejectedRequests = new ArrayList<>(); //Отклонена
}