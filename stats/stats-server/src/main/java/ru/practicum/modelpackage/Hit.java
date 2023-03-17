package ru.practicum.modelpackage;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hits")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; //Идентификатор записи

    @Column(name = "app")
    private String app; //Идентификатор сервиса для которого записывается информация

    @Column(name = "uri")
    private String uri; //URI для которого был осуществлен запрос

    @Column(name = "ip")
    private String ip; //IP-адрес пользователя, осуществившего запрос

    @Column(name = "timestamp")
    private LocalDateTime timestamp; //Дата и время, когда был совершен запрос к
    // эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
}
