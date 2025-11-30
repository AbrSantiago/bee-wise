package com.beewise.exception;

public class ChallengeNotPendingException extends RuntimeException {
    public ChallengeNotPendingException(String message) {
        super(message);
    }
}
