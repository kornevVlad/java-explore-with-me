package ru.practicum.model.comment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.comment.dto.CommentModerationDto;
import ru.practicum.model.comment.dto.RequestCommentDto;
import ru.practicum.model.comment.dto.ResponseCommentDto;
import ru.practicum.model.comment.mapper.CommentMapper;
import ru.practicum.model.comment.model.Comment;
import ru.practicum.model.comment.repository.CommentRepository;
import ru.practicum.model.comment.status.StatusComment;
import ru.practicum.model.event.model.Event;
import ru.practicum.model.event.repository.EventRepository;
import ru.practicum.model.event.status_event.StatusEvent;
import ru.practicum.model.participation.model.ParticipationRequest;
import ru.practicum.model.participation.repository.RequestRepository;
import ru.practicum.model.participation.status_request.StatusRequest;
import ru.practicum.model.user.model.User;
import ru.practicum.model.user.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              CommentMapper commentMapper,
                              UserRepository userRepository,
                              EventRepository eventRepository,
                              RequestRepository requestRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
    }


    @Override
    @Transactional
    public ResponseCommentDto createComment(Long userId, Long eventId, RequestCommentDto requestCommentDto) {
        User user = getValidUser(userId);
        Event event = getEvent(eventId);
        checkStatusConfirmed(user); //проверка user по статусу
        Comment comment = commentMapper.toComment(user, event, requestCommentDto);
        comment.setStatus(StatusComment.PENDING); //дефолт значение при создании
        log.info("Комментарий перед сохранением {}", comment);
        return commentMapper.toResponseCommentDto(commentRepository.save(comment));
    }

    @Override
    public ResponseCommentDto updateComment(Long userId, Long commentId, RequestCommentDto requestCommentDto) {
        //обновить комментарий в статусах PENDING и REJECTED
        User user = getValidUser(userId);
        Comment comment = getComment(commentId);
        if (comment.getStatus().equals(StatusComment.PUBLISH)) {
            throw new BadRequestException("We do not change the published comment");
        }
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Non-user comment");
        }
        comment.setComment(requestCommentDto.getComment());
        comment.setStatus(StatusComment.PENDING);
        return commentMapper.toResponseCommentDto(commentRepository.save(comment));
    }

    @Override
    public ResponseCommentDto getCommentByUserId(Long userId, Long commentId) {
        User user = getValidUser(userId);
        Comment comment = getComment(commentId);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("BAD REQUEST USER NO COMMENT");
        }
        log.info("Комментарий пользователя с id={} комментарий={}", userId, comment);
        return commentMapper.toResponseCommentDto(comment);
    }

    @Override
    public List<ResponseCommentDto> getAllCommentByUser(Long userId) {
        User user = getValidUser(userId);
        List<ResponseCommentDto> commentsDto = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByUserId(user.getId());
        if (!comments.isEmpty()) {
            for (Comment comment : comments) {
                commentsDto.add(commentMapper.toResponseCommentDto(comment));
            }
        }
        log.info("Получен список комментариев пользователя с id {} список = {}", userId, commentsDto);
        return commentsDto;
    }

    @Override
    public List<ResponseCommentDto> getAllCommentByEventId(Long eventId) {
        //Получить список комментариев у опубликованных событий
        //При других статусах выводить пустой список
        Event event = getEvent(eventId);
        List<ResponseCommentDto> commentsDto = new ArrayList<>();
        List<Comment> comments;
        if (event.getState().equals(StatusEvent.PUBLISHED)) {
            comments = commentRepository.findAllByEventId(eventId);
            if (!comments.isEmpty()) {
                for (Comment comment : comments) {
                    commentsDto.add(commentMapper.toResponseCommentDto(comment));
                }
            }
        }
        return commentsDto;
    }

    @Override
    public ResponseCommentDto updateModerationStatus(Long commentId, CommentModerationDto commentModerationDto) {
        Comment comment = getComment(commentId);
        //не подходящий статус для модерации
        if (commentModerationDto.getStatus().equals(StatusComment.PENDING)) {
            throw new BadRequestException("BAD REQUEST ADMIN STATUS PENDING");
        }
        //изменять статус комментария при его статусе PENDING
        if (comment.getStatus().equals(StatusComment.PENDING)) {
            //добавление времени при статусе PUBLISH
            if (commentModerationDto.getStatus().equals(StatusComment.PUBLISH)) {
                comment.setStatus(commentModerationDto.getStatus());
                comment.setPublishDateTime(LocalDateTime.now());
            } else {
                comment.setStatus(commentModerationDto.getStatus());
            }
        } else {
            log.error("Не подходящий статус изменения {}", comment.getStatus());
            throw new BadRequestException("BAD REQUEST IMMUTABLE COMMENT STATUS");
        }
        log.info("Комментарий прошел обновление при модерации: {}", comment);
        Comment updateComment = commentRepository.save(comment);
        return commentMapper.toResponseCommentDto(updateComment);
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        //комментарий удаляется в статусе PENDING или REJECTED
        User user = getValidUser(userId);
        Comment comment = getComment(commentId);
        if (comment.getStatus().equals(StatusComment.PUBLISH)) {
            log.error("Опубликованный комментарий не удаляется");
            throw new BadRequestException("The published comment is not deleted");
        }
        if (comment.getUser().getId().equals(user.getId())) {
            commentRepository.deleteById(comment.getId());
        } else {
            log.error("Пользователь {} не оставлял данный комментарий {}", userId, commentId);
            throw new BadRequestException("Non-user comment");
        }
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        //комментрай удаляется в любом статусе
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new NotFoundException("NOT FOUND COMMENT");
        }
        commentRepository.deleteById(commentId);
    }

    private User getValidUser(Long userid) {
        Optional<User> user = userRepository.findById(userid);
        if (user.isEmpty()) {
            throw new NotFoundException("NOT FOUND USER");
        }
        return user.get();
    }

    private Event getEvent(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException("NOT FOUND EVENT");
        }
        return event.get();
    }

    private Comment getComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new NotFoundException("NOT FOUND COMMENT");
        }
        return comment.get();
    }

    private void checkStatusConfirmed(User user) {
        //оставить комментарий только при статусе заявки на участие CONFIRMED
        ParticipationRequest participationRequest = requestRepository
                .findParticipationRequestByRequesterAndStatus(user, StatusRequest.CONFIRMED);
        if (participationRequest == null) {
            throw new BadRequestException("BAD STATUS REQUEST");
        }
    }
}