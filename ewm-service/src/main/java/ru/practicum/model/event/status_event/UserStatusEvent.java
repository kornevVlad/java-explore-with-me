package ru.practicum.model.event.status_event;

public enum UserStatusEvent {

    SEND_TO_REVIEW, //отправить на повторную модерацию PENDING
    CANCEL_REVIEW //закрыт пользователем
}
