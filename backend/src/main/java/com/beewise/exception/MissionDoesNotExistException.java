package com.beewise.exception;

public class MissionDoesNotExistException extends RuntimeException {
    public MissionDoesNotExistException(String message) {
        super(message);
    }
}
