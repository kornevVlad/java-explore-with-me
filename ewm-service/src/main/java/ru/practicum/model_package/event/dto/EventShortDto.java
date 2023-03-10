package ru.practicum.model_package.event.dto;

import lombok.Data;
import ru.practicum.model_package.categories.categoryDto.CategoryDto;
import ru.practicum.model_package.user.userDto.UserShortDto;

@Data
public class EventShortDto {

    private Long id; //Идентификатор
    private String annotation; //Краткое описание

    private CategoryDto categoryDto; //Категория

    private Long confirmedRequests; //Количество одобренных заявок на участие в данном событии

    private String eventDate; //Дата и время на которые намечено событие
                              // (в формате "yyyy-MM-dd HH:mm:ss")

    private UserShortDto initiator; //Пользователь

    private Boolean paid; //Нужно ли оплачивать участие, example: true

    private String title; //Заголовок

    private Integer views; //Количество просмотрев события
}