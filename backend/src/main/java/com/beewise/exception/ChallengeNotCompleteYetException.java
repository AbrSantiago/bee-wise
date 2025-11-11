package com.beewise.exception;

public class ChallengeNotCompleteYetException extends RuntimeException {
    public ChallengeNotCompleteYetException(String message) {
        super(message);
    }
}
