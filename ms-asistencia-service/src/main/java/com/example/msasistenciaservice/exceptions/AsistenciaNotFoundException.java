package com.example.msasistenciaservice.exceptions;

public class AsistenciaNotFoundException extends RuntimeException {
    public AsistenciaNotFoundException(String message) {
        super(message);
    }
}
