package ru.practicum.model.participation.dto;

import lombok.Data;
import ru.practicum.model.participation.status_request.StatusRequest;

@Data
public class ParticipationRequestDto {

    private Long id; //Идентификатор заявки

    private String created; //Дата и время создания заявки

    private Long event; //Идентификатор события

    private Long requester; //Идентификатор пользователя, отправившего заявку

    private StatusRequest status; //Статус заявки
    //example: PENDING
}