package com.beewise.exception;

public class ShopItemDoesNotExistsException extends RuntimeException {
    public ShopItemDoesNotExistsException(String message) {
        super(message);
    }
}
