package ru.practicum.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String massage) {
        super(massage);
    }
}