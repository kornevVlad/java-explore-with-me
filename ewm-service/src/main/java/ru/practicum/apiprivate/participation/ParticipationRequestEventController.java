package ru.practicum.apiprivate.participation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.apiprivate.participation.service.ParticipationRequestService;
import ru.practicum.model.participation.dto.ParticipationRequestDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}")
public class ParticipationRequestEventController {

    private final ParticipationRequestService service;

    public ParticipationRequestEventController(ParticipationRequestService service) {
        this.service = service;
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     */
    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequestByUser(@PathVariable Long userId,
                                                    @RequestParam Long eventId) {
        log.info("POST participationRequestEvent добавление запроса на участие" +
                        " пользователем с userId={}, событие с eventId={}", userId, eventId);
        return service.addRequestByUser(userId, eventId);
    }

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     */
    @GetMapping("/requests")
    public List<ParticipationRequestDto> getRequestByUserId(@NotNull @PathVariable Long userId) {
        log.info("GET participationRequestEvent список запросов на участие пользователем с id={}", userId);
        return service.getRequestByUserId(userId);
    }

    /**
     * Отмена своего запроса на участие в событии
     */
    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("PATCH participationRequestEvent отмена запроса пользователем = {}, заявка={}", userId, requestId);
        return service.cancelRequest(userId, requestId);
    }
}
