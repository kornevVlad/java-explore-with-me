package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stats {

    private String app; //Название сервиса

    private String uri; //URI сервиса

    private Long hits; //Количество просмотров
}
