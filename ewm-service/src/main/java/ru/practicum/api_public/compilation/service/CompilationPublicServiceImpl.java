package ru.practicum.api_public.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model_package.compilation.dto.CompilationDto;
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
public class CompilationPublicServiceImpl implements CompilationPublicService {

    private final CompilationRepository compilationRepository;

    private final CompilationMapper compilationMapper;

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final RequestRepository requestRepository;

    public CompilationPublicServiceImpl(CompilationRepository compilationRepository,
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
    public List<CompilationDto> getAllByFilterPinned(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);

        List<CompilationDto> compilationDtos = new ArrayList<>();
        if (!compilations.isEmpty()) {
            for (Compilation compilation : compilations) {
                System.out.println(compilation);
                if (!compilation.getEvents().isEmpty()) {
                    List<EventShortDto> eventShortDtos = getEvents(compilation.getEvents());
                    compilationDtos.add(compilationMapper.toCompilationDto(compilation, eventShortDtos));
                } else {
                    compilationDtos.add(compilationMapper.toCompilationDto(compilation, new ArrayList<>()));
                }
            }
        }
        return compilationDtos;
    }

    @Override
    public CompilationDto getById(Long id) {
        if (id == null) {
            throw new BadRequestException("Failed to convert value of type java.lang.String" +
                    " to required type long; nested exception is java.lang.NumberFormatException:" +
                    " For input string: ad");
        }
        Compilation compilation = validCompilation(id);
        List<EventShortDto> eventShortDtos = getEvents(compilation.getEvents());
        return compilationMapper.toCompilationDto(compilation, eventShortDtos);
    }

    private Compilation validCompilation(Long id) {
        Optional<Compilation> compilation = compilationRepository.findById(id);
        if (compilation.isEmpty()) {
            throw new NotFoundException("Compilation with id=" + id + " was not found");
        }
        return compilation.get();
    }

    private List<EventShortDto> getEvents(List<Event> events) {
        List<Long> eventsIds = new ArrayList<>();
        for (Event event : events) {
            eventsIds.add(event.getId());
        }
        List<Event> evRepo = eventRepository.getAllByEvents(eventsIds);
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        for (Event event : evRepo) {
            Long count = requestRepository.countConfirmedByEventId(event.getId());
            eventShortDtos.add(eventMapper.toEventShortDto(event, count));
        }
        return eventShortDtos;
    }
}