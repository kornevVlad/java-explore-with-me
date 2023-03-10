package ru.practicum.model_package.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.model_package.compilation.dto.CompilationDto;
import ru.practicum.model_package.compilation.dto.NewCompilationDto;
import ru.practicum.model_package.compilation.model.Compilation;
import ru.practicum.model_package.event.dto.EventShortDto;
import ru.practicum.model_package.event.model.Event;

import java.util.List;

@Component
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> eventShortDtos) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(eventShortDtos);
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
       // compilationDto.setEvents(eventShortDtos);
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }
}
