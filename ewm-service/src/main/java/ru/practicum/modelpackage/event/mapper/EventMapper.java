package ru.practicum.modelpackage.event.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.modelpackage.categories.mapper.CategoryMapper;
import ru.practicum.modelpackage.categories.model.Category;
import ru.practicum.modelpackage.event.dto.EventFullDto;
import ru.practicum.modelpackage.event.dto.EventShortDto;
import ru.practicum.modelpackage.event.dto.NewEventDto;
import ru.practicum.modelpackage.event.model.Event;
import ru.practicum.modelpackage.event.model.Location;
import ru.practicum.modelpackage.event.status_event.StatusEvent;
import ru.practicum.modelpackage.user.mapper.UserMapper;
import ru.practicum.modelpackage.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventMapper {

    private final CategoryMapper categoryMapper;

    private final UserMapper userMapper;

    public EventMapper(CategoryMapper categoryMapper, UserMapper userMapper) {
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
    }

    public Event toEvent(NewEventDto newEventDto, User user, Category category) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());//Краткое описание
        event.setCategory(category);//Категория
        event.setCreatedOn(LocalDateTime.now());//Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
        event.setDescription(newEventDto.getDescription());//Полное описание события
        //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
        event.setEventDate(generateDataTimeToLocalDataTime(newEventDto.getEventDate()));
        event.setInitiator(user);//Пользователь (краткая информация)
        event.setLat(newEventDto.getLocation().getLat());//Широта места проведения события
        event.setLon(newEventDto.getLocation().getLon());//Долгота места проведения события
        event.setPaid(newEventDto.getPaid());//Нужно ли оплачивать участие, example: true
        event.setParticipantLimit(newEventDto.getParticipantLimit());//Ограничение на количество участников.
        //event.setPublishedOn();
        event.setRequestModeration(newEventDto.getRequestModeration());//Нужна ли пре-модерация заявок на участие
        event.setState(StatusEvent.PENDING);//Список состояний жизненного цикла события, PENDING при создании по умалчанию
        event.setTitle(newEventDto.getTitle());//Заголовок события
        return event;
    }

    public EventFullDto toEventFullDto(Event event, Long confirmed) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(categoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(confirmed);
        eventFullDto.setCreatedOn(generateDataTimeToString(event.getCreatedOn()));
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(generateDataTimeToString(event.getEventDate()));
        eventFullDto.setInitiator(userMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(generatedLocation(event.getLat(), event.getLon()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            eventFullDto.setPublishedOn(generateDataTimeToString(event.getPublishedOn()));
        }
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        return eventFullDto;
    }

    public EventShortDto toEventShortDto(Event event, Long confirmed) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(categoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(confirmed); //Количество одобренных заявок на участие в данном событии
        eventShortDto.setEventDate(generateDataTimeToString(event.getEventDate()));
        eventShortDto.setInitiator(userMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        return eventShortDto;
    }

    private Location generatedLocation(Float lat, Float lon) {
        Location location = new Location();
        location.setLat(lat);
        location.setLon(lon);
        return location;
    }

    private String generateDataTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    private LocalDateTime generateDataTimeToLocalDataTime(String dataTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dataTime, formatter);
    }
}