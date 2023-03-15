package ru.practicum.model.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.dto.CommentModerationDto;
import ru.practicum.model.comment.dto.ResponseCommentDto;
import ru.practicum.model.comment.service.CommentService;

@Slf4j
@RestController
@RequestMapping
public class CommentAdminController {

    private final CommentService commentService;

    public CommentAdminController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Модерация комментария администратором с изменением статуса
     */
    @PatchMapping("/admin/comments/{commentId}")
    public ResponseCommentDto updateModerationStatus(@RequestBody CommentModerationDto commentModerationDto,
                                                     @PathVariable Long commentId) {
        log.info("PATCH CommentAdminController обновление статуса комментария с id={} комментарий {}",
                commentId, commentModerationDto);
        return commentService.updateModerationStatus(commentId, commentModerationDto);
    }

    /**
     * Удаление комментария
     */
    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long commentId) {
        log.info("DELETE CommentAdminController удаление комментария по id={}", commentId);
        commentService.deleteCommentByAdmin(commentId);
    }
}
