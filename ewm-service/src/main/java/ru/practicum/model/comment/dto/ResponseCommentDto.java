package ru.practicum.model.comment.dto;

import lombok.Data;
import ru.practicum.model.comment.status.StatusComment;

@Data
public class ResponseCommentDto {

    private Long id;

    private String comment;

    private Long eventId;

    private Long userId;

    private String publishDateTime;

    private StatusComment status;
}