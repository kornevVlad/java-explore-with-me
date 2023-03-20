package ru.practicum.apiadmin.compilation.service;

import ru.practicum.modelpackage.compilation.dto.CompilationDto;
import ru.practicum.modelpackage.compilation.dto.NewCompilationDto;
import ru.practicum.modelpackage.compilation.dto.UpdateCompilationRequestDto;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto updateCompilationRequestDto);
}
