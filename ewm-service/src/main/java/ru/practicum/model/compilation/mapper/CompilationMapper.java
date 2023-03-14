package ru.practicum.model.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.model.event.model.Event;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.NewCompilationDto;
import ru.practicum.model.compilation.model.Compilation;
import ru.practicum.model.event.dto.EventShortDto;

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
