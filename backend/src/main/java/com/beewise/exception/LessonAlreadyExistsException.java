package com.beewise.exception;

public class LessonAlreadyExistsException extends RuntimeException {
    public LessonAlreadyExistsException(String message) {
        super(message);
    }
}
