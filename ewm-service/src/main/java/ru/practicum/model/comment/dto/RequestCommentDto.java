package ru.practicum.model.comment.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RequestCommentDto {

    @NotBlank
    @Size(min = 10, max = 2000)
    private String comment;
}