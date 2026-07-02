package com.emm.mseventoservice.exceptions;

public class OrganizerNotFoundException extends RuntimeException {
    public OrganizerNotFoundException(String message) {
        super(message);
    }
    
    public OrganizerNotFoundException(Long organizerId) {
        super("Organizer not found with ID: " + organizerId);
    }
}
