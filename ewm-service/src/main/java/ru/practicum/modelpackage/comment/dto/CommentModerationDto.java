package ru.practicum.modelpackage.comment.dto;


import lombok.Getter;
import lombok.Setter;
import ru.practicum.modelpackage.comment.status.StatusComment;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class CommentModerationDto {

    @Enumerated(EnumType.STRING)
    private StatusComment status;
}
