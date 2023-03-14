package ru.practicum.model.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {

    private Long id;

    @NotBlank()
    private String name; //имя пользователя

    @Email()
    @NotBlank()
    private String email; //email пользователя
}