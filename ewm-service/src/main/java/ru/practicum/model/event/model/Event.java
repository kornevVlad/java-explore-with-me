package ru.practicum.model.event.model;

import lombok.*;
import ru.practicum.model.categories.model.Category;
import ru.practicum.model.event.status_event.StatusEvent;
import ru.practicum.model.user.model.User;


import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "events")
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Идентификатор

    @Column(name = "annotation")
    private String annotation; //Краткое описание

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category; //Категория

    @Column(name = "created_on")
    private LocalDateTime createdOn; //Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")

    @Column(name = "description")
    private String description; //Полное описание события

    @Column(name = "event_date")
    private LocalDateTime eventDate; //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    @Column(name = "published_on")
    private LocalDateTime publishedOn; //Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")

    @ManyToOne()
    @JoinColumn(name = "initiator_id")
    private User initiator; //Пользователь (краткая информация)

    @Column(name = "lat")
    private Float lat; //Широта места проведения события

    @Column(name = "lon")
    private Float lon; //Долгота места проведения события

    @Column(name = "paid")
    private Boolean paid; //Нужно ли оплачивать участие, example: true

    @Column(name = "participant_limit")
    private Long participantLimit; //Ограничение на количество участников.
    // Значение 0 - означает отсутствие ограничения
    //example: 10
    //default: 0

    @Column(name = "request_moderation")
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие
    //example: true
    //default: true

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private StatusEvent state; //Список состояний жизненного цикла события
    //example: PUBLISHED
    //Enum:[ PENDING - ожидающий, PUBLISHED - опубликованный, CANCELED - завершен ]

    @Column(name = "title")
    private String title; //Заголовок события
}