package ru.practicum.modelpackage.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.modelpackage.event.model.Event;
import ru.practicum.modelpackage.compilation.dto.CompilationDto;
import ru.practicum.modelpackage.compilation.dto.NewCompilationDto;
import ru.practicum.modelpackage.compilation.model.Compilation;
import ru.practicum.modelpackage.event.dto.EventShortDto;

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
