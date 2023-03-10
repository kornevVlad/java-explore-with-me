package ru.practicum.api_admin.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model_package.compilation.dto.CompilationDto;
import ru.practicum.model_package.compilation.dto.NewCompilationDto;
import ru.practicum.model_package.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.model_package.compilation.mapper.CompilationMapper;
import ru.practicum.model_package.compilation.model.Compilation;
import ru.practicum.model_package.compilation.repository.CompilationRepository;
import ru.practicum.model_package.event.dto.EventShortDto;
import ru.practicum.model_package.event.mapper.EventMapper;
import ru.practicum.model_package.event.model.Event;
import ru.practicum.model_package.event.repository.EventRepository;
import ru.practicum.model_package.participation_request.repository.RequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final CompilationMapper compilationMapper;

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final RequestRepository requestRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository,
                                  CompilationMapper compilationMapper,
                                  EventRepository eventRepository,
                                  EventMapper eventMapper,
                                  RequestRepository requestRepository) {
        this.compilationRepository = compilationRepository;
        this.compilationMapper = compilationMapper;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.requestRepository = requestRepository;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.getAllByEvents(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        List<EventShortDto> eventShortDtos = generateEventsShortDtoList(events);
        log.info("Сохраненная подборка событий {}", compilation);
        try {
            return compilationMapper.toCompilationDto(compilationRepository.save(compilation), eventShortDtos);
        } catch (RuntimeException e) {
            throw new ConflictException("could not execute statement; SQL [n/a];" +
                    " constraint [uq_compilation_name]; nested exception is " +
                    "org.hibernate.exception.ConstraintViolationException: could not execute statement");
        }
    }

    @Override
    public void deleteCompilation(Long compId) {
        validCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto updateCompilationRequestDto) {
        Compilation compilation = validCompilation(compId);
        List<Event> events = new ArrayList<>();
        if (updateCompilationRequestDto.getEvents() != null) {
            events = eventRepository.getAllByEvents(updateCompilationRequestDto.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationRequestDto.getPinned() != null) {
            compilation.setPinned(updateCompilationRequestDto.getPinned());
        }
        if (updateCompilationRequestDto.getTitle() != null) {
            compilation.setTitle(updateCompilationRequestDto.getTitle());
        }
        List<EventShortDto> eventShortDtos = generateEventsShortDtoList(events);
        log.info("Сохраненная подборка событий {}", compilation);
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation), eventShortDtos);
    }

    private Compilation validCompilation(Long id) {
        Optional<Compilation> compilation = compilationRepository.findById(id);
        if (compilation.isEmpty()) {
            throw new NotFoundException("Compilation with id=" + id + " was not found");
        }
        return compilation.get();
    }

    private List<EventShortDto> generateEventsShortDtoList(List<Event> events) {
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        for (Event event : events) {
            Long count = requestRepository.countConfirmedByEventId(event.getId());
            eventShortDtos.add(eventMapper.toEventShortDto(event, count));
        }
        return eventShortDtos;
    }
}