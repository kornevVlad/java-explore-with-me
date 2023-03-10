package ru.practicum.api_admin.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.api_admin.user.service.AdmUserService;
import ru.practicum.model_package.user.userDto.UserDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
public class AdminUserController {

    private final AdmUserService admUserService;

    public AdminUserController(AdmUserService admUserService) {
        this.admUserService = admUserService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST AdminUser сохранение user = {}", userDto);
        return admUserService.createUser(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("DELETE AdminUser удаление user по id={}", userId);
        admUserService.deleteUser(userId);
    }

    @GetMapping()
    public List<UserDto> getAllUser(@RequestParam List<Long>ids,
                                    @RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET AdminUser получение списка user_id = {}",ids);
        return admUserService.getUsersByIds(ids, from, size);
    }
}