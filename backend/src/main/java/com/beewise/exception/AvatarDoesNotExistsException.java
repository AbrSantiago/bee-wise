package com.beewise.exception;

public class AvatarDoesNotExistsException extends RuntimeException {
    public AvatarDoesNotExistsException(String message) {
        super(message);
    }
}
