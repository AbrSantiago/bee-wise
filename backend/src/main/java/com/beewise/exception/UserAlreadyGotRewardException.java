package com.beewise.exception;

public class UserAlreadyGotRewardException extends RuntimeException {
    public UserAlreadyGotRewardException(String message) {
        super(message);
    }
}
