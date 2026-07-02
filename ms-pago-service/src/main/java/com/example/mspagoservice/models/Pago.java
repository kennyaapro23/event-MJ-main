package com.example.mspagoservice.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pagoId;

    @Column(nullable = false)
    private Long registroId;

    @Column(nullable = false)
    private Long participantId;

    @Column(nullable = false)
    private Long eventoId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MetodoPago metodoPago = MetodoPago.TARJETA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @Column(nullable = false)
    private LocalDateTime fechaPago;

    @Column(length = 255)
    private String referencia;

    public enum MetodoPago {
        TARJETA,
        YAPE,
        PLIN,
        EFECTIVO
    }

    public enum EstadoPago {
        PENDIENTE,
        PAGADO,
        FALLIDO,
        REEMBOLSADO
    }
}
