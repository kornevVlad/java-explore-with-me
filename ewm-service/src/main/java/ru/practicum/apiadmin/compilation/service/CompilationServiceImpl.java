package ru.practicum.apiadmin.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.client.Client;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.NewCompilationDto;
import ru.practicum.model.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.model.compilation.mapper.CompilationMapper;
import ru.practicum.model.compilation.model.Compilation;
import ru.practicum.model.compilation.repository.CompilationRepository;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.mapper.EventMapper;
import ru.practicum.model.event.model.Event;
import ru.practicum.model.event.repository.EventRepository;
import ru.practicum.model.participation.repository.RequestRepository;

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

    private final Client client;

    public CompilationServiceImpl(CompilationRepository compilationRepository,
                                  CompilationMapper compilationMapper,
                                  EventRepository eventRepository,
                                  EventMapper eventMapper,
                                  RequestRepository requestRepository,
                                  Client client) {
        this.compilationRepository = compilationRepository;
        this.compilationMapper = compilationMapper;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.requestRepository = requestRepository;
        this.client = client;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.getAllByEvents(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        List<EventShortDto> eventShortDtos = generateEventsShortDtoList(events);
        log.info("Сохраненная подборка событий {}", compilation);
        try {
            return compilationMapper.toCompilationDto(compilationRepository.save(compilation),
                    client.setViewsToEventsShortDto(eventShortDtos));
        } catch (RuntimeException e) {
            throw new ConflictException("CONFLICT SAVE COMPILATION");
        }
    }

    @Override
    public void deleteCompilation(Long compId) {
        getCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto updateCompilationRequestDto) {
        Compilation compilation = getCompilation(compId);
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
        client.setViewsToEventsShortDto(eventShortDtos);
        log.info("Сохраненная подборка событий {}", compilation);
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation),
                client.setViewsToEventsShortDto(eventShortDtos));
    }

    private Compilation getCompilation(Long id) {
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