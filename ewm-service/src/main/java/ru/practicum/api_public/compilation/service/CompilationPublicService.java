package ru.practicum.api_public.compilation.service;

import ru.practicum.model_package.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {

    List<CompilationDto> getAllByFilterPinned(Boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long id);
}
