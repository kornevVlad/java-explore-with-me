package ru.practicum.api_admin.category.service;

import ru.practicum.model_package.categories.categoryDto.CategoryDto;
import ru.practicum.model_package.categories.categoryDto.NewCategoryDto;

public interface AdmCategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long id);

    CategoryDto updateCategory(NewCategoryDto newCategoryDto, Long id);
}
