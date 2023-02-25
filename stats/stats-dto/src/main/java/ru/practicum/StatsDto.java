package ru.practicum;

import lombok.Data;

@Data
public class StatsDto {

    private String app; //Название сервиса

    private String uri; //URI сервиса

    private Long hits; //Количество просмотров
}