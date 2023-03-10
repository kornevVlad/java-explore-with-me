package ru.practicum.model_package.participation_request.model;

import lombok.Data;
import ru.practicum.model_package.participation_request.status_request.StatusRequest;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds; //Идентификаторы запросов на участие в событии текущего пользователя

    private StatusRequest status; //Новый статус запроса на участие в событии текущего пользователя
      //CONFIRMED
      //REJECTED
}
