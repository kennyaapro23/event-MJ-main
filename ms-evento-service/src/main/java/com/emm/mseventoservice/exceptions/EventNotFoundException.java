package com.emm.mseventoservice.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
    
    public EventNotFoundException(Long eventId) {
        super("Event not found with ID: " + eventId);
    }
}