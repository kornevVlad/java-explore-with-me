package ru.practicum.apipublic.compilation;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.apipublic.compilation.service.CompilationPublicService;
import ru.practicum.model.compilation.dto.CompilationDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
public class CompilationPublicController {

    private final CompilationPublicService compilationPublicService;

    public CompilationPublicController(CompilationPublicService compilationPublicService) {
        this.compilationPublicService = compilationPublicService;
    }


    /**
     * Получение подборок событий
     */
    @GetMapping
    public List<CompilationDto> getAllByFilterPinned(@RequestParam(defaultValue = "false") Boolean pinned,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET CompilationPublicController получение списка по фильтрам pinned={}", pinned);
        return compilationPublicService.getAllByFilterPinned(pinned, from, size);
    }

    /**
     * Получение подборки событий по его id
     */
    @GetMapping("/{compId}")
    public CompilationDto getById(@NotNull @PathVariable Long compId) {
        log.info("GET CompilationPublicController получение подборки по id={}", compId);
        return compilationPublicService.getById(compId);
    }
}