package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDto;
import ru.practicum.model.Stats;
import ru.practicum.service.StatsService;

import java.util.List;

@RequestMapping
@RestController
@Slf4j
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping(value = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody HitDto hitDto) {
        log.info("POST статистики, body = {}", hitDto);
        statsService.createHit(hitDto);
    }

    @GetMapping(value = "/stats")
    public List<Stats> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("GET статистики, список выгрузки uri {}", uris);
        return statsService.getStats(start, end, uris, unique);
    }
}
