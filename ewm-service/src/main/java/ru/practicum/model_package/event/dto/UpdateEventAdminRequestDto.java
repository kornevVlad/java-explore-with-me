package ru.practicum.model_package.event.dto;

import lombok.Data;
import ru.practicum.model_package.event.model.Location;

import javax.validation.constraints.Size;

@Data
public class UpdateEventAdminRequestDto {

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
        //example: false


    private String stateAction; //Новое состояние события
    //Enum:
    //[ PUBLISH_EVENT - ПУБЛИКУЕМОЕ СОБЫТИЕ, REJECT_EVENT - ОТКЛОНИТЬ СОБЫТИЕ ]

    @Size(min = 3, max = 120)
    private String title; //Новый заголовок
}