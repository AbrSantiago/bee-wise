package com.beewise.exception;

public class ItemAlreadyBoughtException extends RuntimeException {
    public ItemAlreadyBoughtException(String message) {
        super(message);
    }
}
