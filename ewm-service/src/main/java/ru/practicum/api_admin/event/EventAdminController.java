package ru.practicum.api_admin.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api_admin.event.service.EventAdminService;
import ru.practicum.model_package.event.dto.EventFullDto;
import ru.practicum.model_package.event.dto.UpdateEventAdminRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventAdminService eventAdminService;

    public EventAdminController(EventAdminService eventAdminService) {
        this.eventAdminService = eventAdminService;
    }

    /**
     * Редактирование данных события и его статуса (отклонение/публикация).
     */
    @PatchMapping("/{eventId}")
    public EventFullDto updateEventAdmin(@PathVariable Long eventId,
                                         @RequestBody UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        log.info("PATCH EventAdminController изменение события администратором событие id={}, event={}",
                eventId, updateEventAdminRequestDto);
        return eventAdminService.updateEventAdmin(eventId, updateEventAdminRequestDto);
    }

    /**
     *Поиск событий
     */
   @GetMapping
    public List<EventFullDto> getAllEventsByFilter(@RequestParam(required = false) List<Long> users,
                                                   @RequestParam(required = false) List<String> states,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(required = false) String rangeStart,
                                                   @RequestParam(required = false) String rangeEnd,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET EventAdminController получение списка администратом по заданному фильтру");
        return eventAdminService.getAllEventsByFilter(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
