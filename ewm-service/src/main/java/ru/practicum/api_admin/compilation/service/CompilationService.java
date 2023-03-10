package ru.practicum.api_admin.compilation.service;

import ru.practicum.model_package.compilation.dto.CompilationDto;
import ru.practicum.model_package.compilation.dto.NewCompilationDto;
import ru.practicum.model_package.compilation.dto.UpdateCompilationRequestDto;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto updateCompilationRequestDto);
}
