package ru.practicum.modelpackage.participation.model;


import lombok.Getter;
import lombok.Setter;
import ru.practicum.modelpackage.participation.status_request.StatusRequest;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds; //Идентификаторы запросов на участие в событии текущего пользователя

    private StatusRequest status; //Новый статус запроса на участие в событии текущего пользователя
      //CONFIRMED
      //REJECTED
}
