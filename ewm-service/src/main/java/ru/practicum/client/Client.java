package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.StatsClient;
import ru.practicum.StatsDto;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Client {

    private final StatsClient statsClient;


    public List<EventFullDto> setViewsToEventsFullDto(List<EventFullDto> eventsFullDto) {
        Set<String> uris = new HashSet<>();
        for (EventFullDto eventFullDto : eventsFullDto) {
            uris.add("/events/" + eventFullDto.getId());
        }

        Map<Long, Long> views = getViewsForEvents(uris);

        eventsFullDto.forEach(e -> e.setViews(views.get(e.getId())));

        return eventsFullDto;
    }

    public List<EventShortDto> setViewsToEventsShortDto(List<EventShortDto> eventsShortDto) {
        Set<String> uris = new HashSet<>();
        for (EventShortDto eventShortDto : eventsShortDto) {
            uris.add("/events/" + eventShortDto.getId());
        }

        Map<Long, Long> views = getViewsForEvents(uris);

        eventsShortDto.forEach(e -> e.setViews(views.get(e.getId())));

        return eventsShortDto;
    }

    public EventFullDto setViewsToEventFullDto(EventFullDto eventFullDto) {
        List<StatsDto> stats = getViewsForEvent(eventFullDto.getId());

        if (!stats.isEmpty()) {
            eventFullDto.setViews(stats.get(0).getHits());
        }

        return eventFullDto;
    }

    public EventShortDto setViewsToEventShortDto(EventShortDto eventShortDto) {
        List<StatsDto> stats = getViewsForEvent(eventShortDto.getId());

        if (!stats.isEmpty()) {
            eventShortDto.setViews(stats.get(0).getHits());
        }

        return eventShortDto;
    }

    private Map<Long, Long> getViewsForEvents(Set<String> uris) {
        List<StatsDto> stats = statsClient.getAllStats(uris);

        Map<Long, Long> views = new HashMap<>();

        for (StatsDto stat : stats) {
            views.put(
                    Long.parseLong(stat.getUri().replace("/events/", "")),
                    stat.getHits()
            );
        }
        return views;
    }

    private List<StatsDto> getViewsForEvent(Long eventId) {
        Set<String> uri = new HashSet<>();
        uri.add("/events/" + eventId);
        return statsClient.getAllStats(uri);
    }
}