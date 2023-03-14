package ru.practicum.apipublic.categories.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.model.categories.dto.CategoryDto;
import ru.practicum.model.categories.mapper.CategoryMapper;
import ru.practicum.model.categories.model.Category;
import ru.practicum.model.categories.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper mapper;

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryMapper mapper, CategoryRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public List<CategoryDto> getAllCategoriesPagination(Integer from, Integer size) {
        List<CategoryDto> categoriesDto = new ArrayList<>();
        Pageable pageable = PageRequest.of(from, size);
        Page<Category> categories = repository.findAll(pageable);
        categories.stream().forEach(category -> categoriesDto.add(mapper.toCategoryDto(category)));
        log.info("Категории с пагинацией: {}", categoriesDto);
        return categoriesDto;
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        if (id == null) {
            throw new BadRequestException("Failed to convert value of type java.lang.String" +
                    " to required type long; nested exception is java.lang.NumberFormatException:" +
                    " For input string: ad");
        }
        validNotFound(id);
        Category category = repository.getReferenceById(id);
        log.info("Категория по id={}", category);
        return mapper.toCategoryDto(category);
    }

    private void validNotFound(Long id) {
        if (!repository.existsById(id)) {
            log.error("Категория с id {} не найден", id);
            throw new NotFoundException("Category with id=" + id + " was not found");
        }
    }
}