package com.emm.mseventoservice.exceptions;

public class DuplicateSessionException extends RuntimeException {
    public DuplicateSessionException(String message) {
        super(message);
    }
}