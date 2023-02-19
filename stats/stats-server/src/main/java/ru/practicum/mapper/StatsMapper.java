package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.HitDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatsMapper {

    public Hit toHit(HitDto hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setIp(hitDto.getIp());
        hit.setUri(hitDto.getUri());
        hit.setTimestamp(generateDataTime(hitDto.getTimestamp()));
        return hit;
    }

    public HitDto toHitDto(Hit hit) {
        HitDto hitDto = new HitDto();
        hitDto.setId(hit.getId());
        hitDto.setApp(hit.getApp());
        hitDto.setUri(hit.getUri());
        hitDto.setIp(hit.getIp());
        hitDto.setTimestamp(generateDataTimeToString(hit.getTimestamp()));
        return hitDto;
    }

    public LocalDateTime generateDataTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }

    public String generateDataTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }
}