package ru.practicum.model.event.dto;

import lombok.Data;
import ru.practicum.model.event.model.Location;
import ru.practicum.model.event.status_event.UserStatusEvent;

import javax.validation.constraints.Size;

@Data
public class UpdateEventUserRequestDto {

    @Size(min = 20, max = 2000)
    private String annotation; //Новая аннотация

    private Long category; //Новая категория

    @Size(min = 20, max = 7000)
    private String description; //Новое описание

    private String eventDate; //Новые дата и время на которые намечено событие.
    // Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"

    private Location location; //Широта и долгота места проведения события

    private Boolean paid; //Новое значение флага о платности мероприятия
        //example: true

    private Long participantLimit; //Новый лимит пользователей

    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие

    private UserStatusEvent stateAction; //Изменение сотояния события
    //Enum:
    //[ SEND_TO_REVIEW, CANCEL_REVIEW ]

    @Size(min = 3, max = 120)
    private String title; //Новый заголовок
}