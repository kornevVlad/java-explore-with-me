package ru.practicum.api_public.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api_public.event.service.EventPublicService;
import ru.practicum.model_package.event.dto.EventFullDto;
import ru.practicum.model_package.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
public class EventPublicController {

    private final EventPublicService eventPublicService;

    public EventPublicController(EventPublicService eventPublicService) {
        this.eventPublicService = eventPublicService;
    }

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     * Вызов эндпоинта сохранить в статистику
     */
    @GetMapping("/{id}")
    public EventFullDto getEventByEventId(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        log.info("GET EventPublicController запрос события по id={}", id);
        return eventPublicService.getEventByEventId(id);
    }

    /**
     *
     * Получение событий с возможностью фильтрации
     */
    @GetMapping
    public List<EventShortDto> getAllEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET EventPublicController получение событий с фильтрацией");
        return eventPublicService.getAllEventsByStatusPublic(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
    }
}
