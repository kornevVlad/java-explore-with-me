package ru.practicum.model.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.model.comment.dto.RequestCommentDto;
import ru.practicum.model.comment.dto.ResponseCommentDto;
import ru.practicum.model.comment.model.Comment;
import ru.practicum.model.comment.status.StatusComment;
import ru.practicum.model.event.model.Event;
import ru.practicum.model.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CommentMapper {

    public Comment toComment(User user, Event event, RequestCommentDto commentDto) {
        Comment comment = new Comment();
        comment.setComment(commentDto.getComment());
        comment.setUser(user);
        comment.setEvent(event);
        return comment;
    }

    public ResponseCommentDto toResponseCommentDto(Comment comment) {
        ResponseCommentDto responseCommentDto = new ResponseCommentDto();
        responseCommentDto.setId(comment.getId());
        responseCommentDto.setComment(comment.getComment());
        responseCommentDto.setEventId(comment.getEvent().getId());
        responseCommentDto.setUserId(comment.getUser().getId());
        if (comment.getPublishDateTime() != null) {
            responseCommentDto.setPublishDateTime(generateDataTimeToString(comment.getPublishDateTime()));
        }
        responseCommentDto.setStatus(comment.getStatus());
        return responseCommentDto;
    }

    private String generateDataTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}