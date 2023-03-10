package ru.practicum.api_admin.user.service;

import ru.practicum.model_package.user.userDto.UserDto;

import java.util.List;

public interface AdmUserService {

    /**
     * Создание пользователя
     */
    UserDto createUser(UserDto userDto);

    /**
     * Удаление пользователя
     */
    void deleteUser(Long userId);

    /**
     * Получение списка пользователей по id пользователя
     */
    List<UserDto> getUsersByIds(List<Long> ids, Integer from, Integer size);
}