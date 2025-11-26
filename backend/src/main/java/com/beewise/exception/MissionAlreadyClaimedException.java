package com.beewise.exception;

public class MissionAlreadyClaimedException extends RuntimeException {
    public MissionAlreadyClaimedException(String message) {
        super(message);
    }
}
