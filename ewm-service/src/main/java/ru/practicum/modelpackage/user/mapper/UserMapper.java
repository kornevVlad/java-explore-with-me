package ru.practicum.modelpackage.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.modelpackage.user.dto.UserDto;
import ru.practicum.modelpackage.user.model.User;
import ru.practicum.modelpackage.user.dto.UserShortDto;

@Component
public class UserMapper {

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }
}