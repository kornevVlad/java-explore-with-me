package ru.practicum.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.response_status_code.HttpStatusCode;

import java.time.LocalDateTime;

@RestControllerAdvice
@Data
public class ErrorResponse {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError errorNotFound(final NotFoundException e) {
        ApiError apiError = new ApiError();
        apiError.setErrors(HttpStatusCode.NOT_FOUND.toString());
        apiError.setMessage(e.getMessage());
        apiError.setStatus("404");
        apiError.setTimestamp(LocalDateTime.now().toString());
        return apiError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiError errorBadRequest(final BadRequestException e) {
        ApiError apiError = new ApiError();
        apiError.setErrors(HttpStatusCode.BAD_REQUEST.toString());
        apiError.setMessage(e.getMessage());
        apiError.setStatus("400");
        apiError.setTimestamp(LocalDateTime.now().toString());
        return apiError;
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError errorConflict(final ConflictException e) {
        ApiError apiError = new ApiError();
        apiError.setErrors(HttpStatusCode.CONFLICT.toString());
        apiError.setMessage(e.getMessage());
        apiError.setStatus("409");
        apiError.setTimestamp(LocalDateTime.now().toString());
        return apiError;
    }
}