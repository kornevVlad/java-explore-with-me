package ru.practicum.apiadmin.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.apiadmin.category.service.AdmCategoryService;
import ru.practicum.model.categories.dto.CategoryDto;
import ru.practicum.model.categories.dto.NewCategoryDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final AdmCategoryService service;

    public AdminCategoryController(AdmCategoryService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("POST AdminCategory добавление категории = {}", newCategoryDto);
        return service.createCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("DELETE AdminCategory удаление категории по id={}", catId);
        service.deleteCategory(catId);
    }

    @PatchMapping("{catId}")
    public CategoryDto updateCategory(@Valid @RequestBody NewCategoryDto newCategoryDto,
                                                        @PathVariable Long catId) {
        log.info("PATCH AdminCategory изменение категории id={} name={} ", catId, newCategoryDto);
        return service.updateCategory(newCategoryDto, catId);
    }
}