package ru.practicum.apiadmin.category.service;

import ru.practicum.modelpackage.categories.dto.CategoryDto;
import ru.practicum.modelpackage.categories.dto.NewCategoryDto;

public interface AdmCategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long id);

    CategoryDto updateCategory(NewCategoryDto newCategoryDto, Long id);
}
