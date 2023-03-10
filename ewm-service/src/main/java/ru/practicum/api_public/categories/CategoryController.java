package ru.practicum.api_public.categories;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model_package.categories.categoryDto.CategoryDto;
import ru.practicum.api_public.categories.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAllCategories(
                            @RequestParam(defaultValue = "0") Integer from,
                            @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET категории с пагинацией from={} size={}", from, size);
        return service.getAllCategoriesPagination(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("GET категории по id={}", catId);
        return service.getCategoryById(catId);
    }
}