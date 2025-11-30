package com.beewise.exception;

public class ChallengeAlreadyCompletedException extends RuntimeException {
    public ChallengeAlreadyCompletedException(String message) {
        super(message);
    }
}
