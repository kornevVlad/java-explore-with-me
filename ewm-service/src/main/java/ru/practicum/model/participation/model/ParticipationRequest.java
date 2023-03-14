package ru.practicum.model.participation.model;

import lombok.Data;
import ru.practicum.model.event.model.Event;
import ru.practicum.model.participation.status_request.StatusRequest;
import ru.practicum.model.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; //Идентификатор заявки

    @Column(name = "created")
    private LocalDateTime created; //Дата и время создания заявки

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event; //Идентификатор события

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester; //Идентификатор пользователя, отправившего заявку

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusRequest status; //Статус заявки
    //example: PENDING
}
