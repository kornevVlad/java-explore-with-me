package ru.practicum;

import lombok.Data;

@Data
public class HitDto {

    private Long id; //Идентификатор записи

    private String app; //Идентификатор сервиса для которого записывается информация

    private String uri; //URI для которого был осуществлен запрос

    private String ip; //IP-адрес пользователя, осуществившего запрос

    private String timestamp; //Дата и время, когда был совершен запрос к
                                // эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
}
