package ru.practicum.model.categories.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryDto {

    private Long id;

    @NotBlank()
    private String name;
}
