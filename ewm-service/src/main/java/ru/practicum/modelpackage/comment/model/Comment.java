package ru.practicum.modelpackage.comment.model;

import lombok.Data;
import ru.practicum.modelpackage.comment.status.StatusComment;
import ru.practicum.modelpackage.event.model.Event;
import ru.practicum.modelpackage.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "publish")
    private LocalDateTime publishDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusComment status;
}
