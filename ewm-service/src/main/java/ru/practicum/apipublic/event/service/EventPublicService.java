package ru.practicum.apipublic.event.service;


import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventPublicService {

    EventFullDto getEventByEventId(Long eventId, HttpServletRequest httpServletRequest);

    List<EventShortDto> getAllEventsByStatusPublic(String text,
                                                   List<Long> categories,
                                                   Boolean paid,
                                                   String rangeStart,
                                                   String rangeEnd,
                                                   Boolean onlyAvailable,
                                                   String sort,
                                                   Integer from,
                                                   Integer size,
                                                   HttpServletRequest httpServletRequest);
}
