package com.emm.mseventoservice.exceptions;

public class UnauthorizedEventAccessException extends RuntimeException {
    public UnauthorizedEventAccessException(String message) {
        super(message);
    }
    
    public UnauthorizedEventAccessException(Long eventId, Long organizerId) {
        super("Organizer " + organizerId + " is not authorized to modify event " + eventId);
    }
}