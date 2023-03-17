package ru.practicum.modelpackage.categories.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.modelpackage.categories.dto.CategoryDto;
import ru.practicum.modelpackage.categories.dto.NewCategoryDto;
import ru.practicum.modelpackage.categories.model.Category;

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