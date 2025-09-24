package com.beewise.exception;

public class AnswerNotAllowedException extends RuntimeException {
    public AnswerNotAllowedException(String message) {
        super(message);
    }
}
