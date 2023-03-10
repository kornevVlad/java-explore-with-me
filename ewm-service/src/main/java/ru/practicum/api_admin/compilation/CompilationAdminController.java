package ru.practicum.api_admin.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api_admin.compilation.service.CompilationService;
import ru.practicum.model_package.compilation.dto.CompilationDto;
import ru.practicum.model_package.compilation.dto.NewCompilationDto;
import ru.practicum.model_package.compilation.dto.UpdateCompilationRequestDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    public CompilationAdminController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    /**
     * Добавление новой подборки (подборка может не содержать событий)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST CompilationAdminController создани новой подборки {}", newCompilationDto);
        return compilationService.createCompilation(newCompilationDto);
    }

    /**
     * Удаление подборки
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("DELETE CompilationAdminController удаление подборки по id={}", compId);
        compilationService.deleteCompilation(compId);
    }

    /**
     * Обновить информацию о подборке
     */
    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody UpdateCompilationRequestDto updateCompilationRequestDto) {
        log.info("PATCH CompilationAdminController" +
                " обновление подборки с id={}, новая подборка = {}", compId, updateCompilationRequestDto);
        return compilationService.updateCompilation(compId, updateCompilationRequestDto);
    }

}
