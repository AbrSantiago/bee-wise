package com.beewise.exception;

public class UserNotPlayingChallengeException extends RuntimeException {
    public UserNotPlayingChallengeException(String message) {
        super(message);
    }
}
