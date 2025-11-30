package com.beewise.exception;

public class MissionProgressDoesNotExistException extends RuntimeException {
    public MissionProgressDoesNotExistException(String message) {
        super(message);
    }
}
