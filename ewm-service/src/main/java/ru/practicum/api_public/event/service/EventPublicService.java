package ru.practicum.api_public.event.service;


import ru.practicum.model_package.event.dto.EventFullDto;
import ru.practicum.model_package.event.dto.EventShortDto;

import java.util.List;

public interface EventPublicService {

    EventFullDto getEventByEventId(Long eventId);

    List<EventShortDto> getAllEventsByStatusPublic(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                   String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);
}
