package ru.practicum.model_package.categories.categoryDto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewCategoryDto {

    @NotBlank()
    private String name;
}
