package ru.practicum.modelpackage.compilation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class UpdateCompilationRequestDto {

    private List<Long> events; //Список id событий подборки для полной замены текущего списка

    @Builder.Default
    private Boolean pinned = false; //Закреплена ли подборка на главной странице сайта

    @NotBlank
    private String title; //Заголовок подборки
}