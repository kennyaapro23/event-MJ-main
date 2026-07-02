package com.example.msasistenciaservice.exceptions;

public class QrNotFoundException extends RuntimeException {
    public QrNotFoundException(String message) {
        super(message);
    }
}
