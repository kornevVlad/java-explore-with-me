package ru.practicum.modelpackage.user.dto;

import lombok.Data;

@Data
public class UserShortDto {

    /**
     * Коротний пользователь
     */
    private Long id;

    private String name;
}
