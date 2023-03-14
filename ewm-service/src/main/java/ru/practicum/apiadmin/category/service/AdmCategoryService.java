package ru.practicum.apiadmin.category.service;

import ru.practicum.model.categories.dto.CategoryDto;
import ru.practicum.model.categories.dto.NewCategoryDto;

public interface AdmCategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long id);

    CategoryDto updateCategory(NewCategoryDto newCategoryDto, Long id);
}
