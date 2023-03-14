package ru.practicum.apipublic.categories.service;

import ru.practicum.model.categories.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategoriesPagination(Integer from, Integer size);

    CategoryDto getCategoryById(Long id);
}
