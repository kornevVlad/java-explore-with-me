package ru.practicum.api_admin.event.service;

import ru.practicum.model_package.event.dto.EventFullDto;
import ru.practicum.model_package.event.dto.UpdateEventAdminRequestDto;

import java.util.List;

public interface EventAdminService {

    EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);

    List<EventFullDto> getAllEventsByFilter(List<Long> users, List<String> states,
                                            List<Long> categories, String rangeStart, String rangeEnd,
                                            Integer from, Integer size);
}
