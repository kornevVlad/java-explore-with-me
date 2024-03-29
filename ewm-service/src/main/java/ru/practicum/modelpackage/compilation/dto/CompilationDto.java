package ru.practicum.modelpackage.compilation.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.modelpackage.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
public class CompilationDto {

    private List<EventShortDto> events; //Закреплена ли подборка на главной странице сайта
    private Long id; //Идентификатор

    private Boolean pinned; //Закреплена ли подборка на главной странице сайта

    private String title; //Заголовок подборки
}