package com.example.mspagoservice.dtos;

import com.example.mspagoservice.models.Pago;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    private Long pagoId;
    private Long registroId;
    private Long participantId;
    private Long eventoId;
    private BigDecimal monto;
    private Pago.MetodoPago metodoPago;
    private Pago.EstadoPago estado;
    private LocalDateTime fechaPago;
    private String referencia;
}
