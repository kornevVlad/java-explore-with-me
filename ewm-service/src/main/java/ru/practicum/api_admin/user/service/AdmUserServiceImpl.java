package ru.practicum.api_admin.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model_package.user.mapper.UserMapper;
import ru.practicum.model_package.user.model.User;
import ru.practicum.model_package.user.repository.UserRepository;
import ru.practicum.model_package.user.userDto.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdmUserServiceImpl implements AdmUserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Autowired
    public AdmUserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        log.info("User в сервисе перед сохранением = {}", user);
        try {
            return userMapper.toUserDto(userRepository.save(user));
        } catch (RuntimeException e) {
            throw new ConflictException("could not execute statement;" +
                    " SQL [n/a]; constraint [uq_email];" +
                    " nested exception is org.hibernate.exception.ConstraintViolationException:" +
                    " could not execute statement");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Удаление user по id в сервисе");
        validUser(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsersByIds(List<Long>ids, Integer from, Integer size) {
        List<UserDto> usersDto = new ArrayList<>();
        Pageable pageable = PageRequest.of(from, size);
        if (ids == null | ids.isEmpty() | ids.size() == 0) {
            throw new BadRequestException("Failed to convert value of type java.lang.String" +
                    " to required type int; nested exception is java.lang.NumberFormatException:" +
                    " For input string: ad");
        }
        List<User> users = userRepository.findByIdIsIn(ids, pageable);
        for (User user : users) {
            usersDto.add(userMapper.toUserDto(user));
        }
        log.info("Получен список пользователей users = {}", usersDto);
        return usersDto;
    }

    private void validUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.info("Пользователь не найден с id={}", userId);
            throw new NotFoundException("User with id="+ userId + " was not found");
        }
    }
}