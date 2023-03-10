package ru.practicum.api_public.categories.service;

import ru.practicum.model_package.categories.categoryDto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategoriesPagination(Integer from, Integer size);

    CategoryDto getCategoryById(Long id);
}
