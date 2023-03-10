package ru.practicum.model_package.participation_request.dto;

import lombok.Data;

@Data
public class ParticipationRequestDto {

    private Long id; //Идентификатор заявки

    private String created; //Дата и время создания заявки

    private Long event; //Идентификатор события

    private Long requester; //Идентификатор пользователя, отправившего заявку

    private String status; //Статус заявки
    //example: PENDING
}