package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatisticRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatisticRepository repository;

    private  final StatsMapper statsMapper;

    @Autowired
    public StatsServiceImpl(StatisticRepository repository, StatsMapper statsMapper) {
        this.repository = repository;
        this.statsMapper = statsMapper;
    }

    @Override
    public HitDto createHit(HitDto hitDto) {
        Hit hit = statsMapper.toHit(hitDto);
        log.info("Экземпляр Hit в сервисе сохранения {}", hit);
        repository.save(hit);
        return statsMapper.toHitDto(hit);
    }

    @Override
    public List<Stats> getStats(String start, String end, List<String> uris, Boolean unique) {
        log.info("Get параметры в сервисе start={}, end={}, unique={}",start,end,unique);
        LocalDateTime startDateTime = getTimeDecoder(start);
        LocalDateTime endDateTime = getTimeDecoder(end);

        List<Stats> statsList;
        if (unique) {
            statsList = repository.getAllByUriAndUniqueIp(startDateTime, endDateTime, uris);
        } else {
            statsList = repository.getAllByUriNotUniqueIp(startDateTime, endDateTime, uris);
        }
        log.info("Список stats {}", statsList);
        return statsList;
    }

    private LocalDateTime getTimeDecoder(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(URLDecoder.decode(dateTime, StandardCharsets.UTF_8), formatter);
    }
}