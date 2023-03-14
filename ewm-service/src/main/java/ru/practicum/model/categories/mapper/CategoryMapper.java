package ru.practicum.model.categories.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.model.categories.dto.CategoryDto;
import ru.practicum.model.categories.dto.NewCategoryDto;
import ru.practicum.model.categories.model.Category;

@Component
public class CategoryMapper {

    public Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}