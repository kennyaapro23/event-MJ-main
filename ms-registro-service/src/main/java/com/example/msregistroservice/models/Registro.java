package com.example.msregistroservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registro")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAttendance;

    private LocalDateTime timestamp;

    private String observations;

    private String idEvento;

    // Solo guardamos el ID del participante
    private Long participantId;
}
