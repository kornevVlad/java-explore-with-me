package ru.practicum.modelpackage.event.dto;

import lombok.Data;
import ru.practicum.modelpackage.event.model.Location;
import ru.practicum.modelpackage.categories.dto.CategoryDto;
import ru.practicum.modelpackage.event.status_event.StatusEvent;
import ru.practicum.modelpackage.user.dto.UserShortDto;

@Data
public class EventFullDto {

    private Long id; //Идентификатор
    private String annotation; //Краткое описание

    private CategoryDto category; //Категория

    private Long confirmedRequests; //Количество одобренных заявок на участие в данном событии

    private String createdOn; //Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")

    private String description; //Полное описание события

    private String eventDate; //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    private UserShortDto initiator; //Пользователь (краткая информация)

    private Location location; //Широта и долгота места проведения события

    private Boolean paid; //Нужно ли оплачивать участие, example: true

    private Long participantLimit; //Ограничение на количество участников.
    // Значение 0 - означает отсутствие ограничения
    //example: 10
    //default: 0

    private String publishedOn; //Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")


    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие
    //example: true
    //default: true


    private StatusEvent state; //Список состояний жизненного цикла события
    //example: PUBLISHED
    //Enum:[ PENDING, PUBLISHED, CANCELED ]


    private Long views; //Количество просмотрев события

    private String title; //Заголовок события
}