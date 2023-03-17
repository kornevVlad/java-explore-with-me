package ru.practicum.modelpackage.comment.service;

import ru.practicum.modelpackage.comment.dto.CommentModerationDto;
import ru.practicum.modelpackage.comment.dto.RequestCommentDto;
import ru.practicum.modelpackage.comment.dto.ResponseCommentDto;

import java.util.List;

public interface CommentService {

    ResponseCommentDto createComment(Long userId, Long eventId, RequestCommentDto requestCommentDto);

    ResponseCommentDto updateComment(Long userId, Long commentId, RequestCommentDto requestCommentDto);

    ResponseCommentDto getCommentByUserId(Long userId, Long commentId);

    List<ResponseCommentDto> getAllCommentByUser(Long userId);

    List<ResponseCommentDto> getAllCommentByEventId(Long eventId);

    ResponseCommentDto updateModerationStatus(Long commentId, CommentModerationDto commentModerationDto);

    void deleteCommentByUser(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);
}