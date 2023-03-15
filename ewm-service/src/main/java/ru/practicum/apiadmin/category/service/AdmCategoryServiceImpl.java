package ru.practicum.apiadmin.category.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.categories.dto.CategoryDto;
import ru.practicum.model.categories.dto.NewCategoryDto;
import ru.practicum.model.categories.mapper.CategoryMapper;
import ru.practicum.model.categories.model.Category;
import ru.practicum.model.categories.repository.CategoryRepository;
import ru.practicum.model.event.repository.EventRepository;

import java.util.Optional;

@Service
@Slf4j
public class AdmCategoryServiceImpl implements AdmCategoryService {

    private final CategoryRepository repository;

    private final CategoryMapper mapper;

    private final EventRepository eventRepository;

    public AdmCategoryServiceImpl(CategoryRepository repository,
                                  CategoryMapper mapper,
                                  EventRepository eventRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventRepository = eventRepository;
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = mapper.toCategory(newCategoryDto);
        log.info("Категория перед сохранением в Service {}", category);
        try {
            return mapper.toCategoryDto(repository.save(category));
        } catch (RuntimeException e) {
            throw new ConflictException("could not execute statement;" +
                    " SQL [n/a]; constraint [uq_category_name];" +
                    " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                    " could not execute statement");
        }
    }

    @Override
    public void deleteCategory(Long id) {
        getCategory(id);
        Long ids = eventRepository.countByCategoryId(id);
        if (ids > 0) {
            throw new ConflictException("The category is not empty");
        }
        repository.deleteById(id);
    }

    @Override
    public CategoryDto updateCategory(NewCategoryDto newCategoryDto, Long id) {
        Category updateCategory = getCategory(id);
        Category newCategory = mapper.toCategory(newCategoryDto);
        updateCategory.setName(newCategory.getName());
        log.info("Обновленная кетегория = {}", updateCategory);
        try {
            return mapper.toCategoryDto(repository.save(updateCategory));
        } catch (RuntimeException e) {
            throw new ConflictException("could not execute statement;" +
                    " SQL [n/a]; constraint [uq_category_name];" +
                    " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                    " could not execute statement");
        }
    }

    private Category getCategory(Long id) {
        Optional<Category> category = repository.findById(id);
        if (category.isEmpty()) {
            log.error("Категория не найдена id={}", id);
            throw new NotFoundException("Category with id=" + id + " was not found");
        }
        return category.get();
    }
}