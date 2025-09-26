package com.beewise.exception;

public class ChallengeAlreadyExistsException extends RuntimeException {
    public ChallengeAlreadyExistsException(String message) {
        super(message);
    }
}
