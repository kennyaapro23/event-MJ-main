package com.example.msasistenciaservice.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencia")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long asistenciaId;

    @Column(nullable = false)
    private Long registroId;

    @Column(nullable = false)
    private Long participantId;

    @Column(nullable = false)
    private Long eventoId;

    @Column(nullable = false, unique = true, length = 100)
    private String codigoQr;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoAsistencia estado = EstadoAsistencia.AUSENTE;

    private LocalDateTime fechaEntrada;

    @Column(length = 255)
    private String observaciones;

    public enum EstadoAsistencia {
        PRESENTE,
        AUSENTE,
        TARDANZA
    }
}
