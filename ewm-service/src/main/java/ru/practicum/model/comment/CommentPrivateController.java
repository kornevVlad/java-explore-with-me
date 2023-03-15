package ru.practicum.model.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.dto.RequestCommentDto;
import ru.practicum.model.comment.dto.ResponseCommentDto;
import ru.practicum.model.comment.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class CommentPrivateController {

    private final CommentService commentService;

    public CommentPrivateController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Создание комментария
     */
    @PostMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseCommentDto createComment(@PathVariable Long userId,
                                            @PathVariable Long eventId,
                                            @RequestBody RequestCommentDto requestCommentDto) {
        log.info("POST CommentPrivateController создание комментария пользователь id={} событие id={} комментарий ={}",
                userId, eventId, requestCommentDto);
        return commentService.createComment(userId, eventId, requestCommentDto);
    }

    /**
     * Обновление комментария
     */
    @PatchMapping("/users/{userId}/comments/{commentId}")
    public ResponseCommentDto updateComment(@PathVariable Long userId,
                                            @PathVariable Long commentId,
                                            @RequestBody RequestCommentDto requestCommentDto) {
        log.info("PATCH CommentPrivateController обновление комментария пользователя id={} комментарий id={}",
                userId, commentId);
        return commentService.updateComment(userId, commentId, requestCommentDto);
    }

    /**
     * Получение комментария текущего пользователя
     */
    @GetMapping("/users/{userId}/comments/{commentId}")
    public ResponseCommentDto getCommentsById(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("GET CommentPrivateController получение комментария по id={}", commentId);
        return commentService.getCommentByUserId(userId, commentId);
    }

    /**
     * Получение всех комментариев текущего пользователя
     */
    @GetMapping("/users/{userId}/comments")
    public List<ResponseCommentDto> getAllCommentByUserId(@PathVariable Long userId) {
        log.info("GET CommentPrivateController получение списка комментариев пользователся с id={}", userId);
        return commentService.getAllCommentByUser(userId);
    }

    /**
     *  Удаление комментария пользователем
     */
    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("DELETE CommentPrivateController удаление комментария пользователя с id={}", userId);
        commentService.deleteCommentByUser(userId, commentId);
    }
}
