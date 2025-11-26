package com.beewise.exception;

public class MissionNotCompletedException extends RuntimeException {
    public MissionNotCompletedException(String message) {
        super(message);
    }
}
