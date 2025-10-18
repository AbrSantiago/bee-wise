package com.beewise.exception;

public class NotEnoughBeeCoinsException extends RuntimeException {
    public NotEnoughBeeCoinsException(String message) {
        super(message);
    }
}
