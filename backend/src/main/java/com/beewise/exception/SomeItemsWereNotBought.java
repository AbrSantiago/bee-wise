package com.beewise.exception;

public class SomeItemsWereNotBought extends RuntimeException {
    public SomeItemsWereNotBought(String message) {
        super(message);
    }
}
