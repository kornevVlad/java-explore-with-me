package ru.practicum.service;


import ru.practicum.HitDto;
import ru.practicum.modelpackage.Stats;

import java.util.List;


public interface StatsService {

    HitDto createHit(HitDto hitDto);

    List<Stats> getStats(String start, String end, List<String> uris, Boolean unique);
}