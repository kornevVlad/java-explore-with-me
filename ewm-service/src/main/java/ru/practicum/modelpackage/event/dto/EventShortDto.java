package ru.practicum.modelpackage.event.dto;

import lombok.Data;
import ru.practicum.modelpackage.categories.dto.CategoryDto;
import ru.practicum.modelpackage.user.dto.UserShortDto;

@Data
public class EventShortDto {

    private Long id; //Идентификатор
    private String annotation; //Краткое описание

    private CategoryDto category; //Категория

    private Long confirmedRequests; //Количество одобренных заявок на участие в данном событии

    private String eventDate; //Дата и время на которые намечено событие
                              // (в формате "yyyy-MM-dd HH:mm:ss")

    private UserShortDto initiator; //Пользователь

    private Boolean paid; //Нужно ли оплачивать участие, example: true

    private String title; //Заголовок

    private Long views; //Количество просмотрев события
}