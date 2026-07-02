package com.emm.mseventoservice.exceptions;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String message) {
        super(message);
    }
    
    public SessionNotFoundException(Long sessionId) {
        super("Session not found with id: " + sessionId);
    }
}