package ru.practicum.modelpackage.event.dto;

import lombok.Data;
import ru.practicum.modelpackage.event.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation; //Краткое описание события

    @NotNull
    private Long category; //id категории к которой относится событие

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;	//Полное описание события

    @NotBlank
    private String eventDate; // Дата и время на которые намечено событие.
                              // Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"

    @NotNull
    Location location; //Широта и долгота места проведения события

    private Boolean paid; //Нужно ли оплачивать участие в событии
                          //example: true
                          //default: false

    private Long participantLimit; //Ограничение на количество участников.
                                      // Значение 0 - означает отсутствие ограничения
                                      //example: 10
                                      //default: 0


    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически.
    //example: false
    //default: true

    @NotBlank
    @Size(min = 3, max = 120)
    private String title; //Заголовок события
}