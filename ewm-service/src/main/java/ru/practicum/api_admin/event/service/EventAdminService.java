package ru.practicum.api_admin.event.service;

import ru.practicum.model_package.event.dto.EventFullDto;
import ru.practicum.model_package.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.model_package.event.status_event.StatusEvent;

import java.util.List;

public interface EventAdminService {

    EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);

    List<EventFullDto> getAllEventsByFilter(List<Long> users, List<StatusEvent> states,
                                            List<Long> categories, String rangeStart, String rangeEnd,
                                            Integer from, Integer size);
}
