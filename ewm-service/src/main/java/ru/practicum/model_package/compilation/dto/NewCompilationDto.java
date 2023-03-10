package ru.practicum.model_package.compilation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {

    private List<Long> events; //Список идентификаторов событий входящих в подборку

    @Builder.Default
    private Boolean pinned = false; //Закреплена ли подборка на главной странице сайта
        //default: false

    @NotBlank
    private String title; //Заголовок подборки
}