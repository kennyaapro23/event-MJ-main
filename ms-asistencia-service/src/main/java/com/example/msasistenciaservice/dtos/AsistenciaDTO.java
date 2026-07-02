package com.example.msasistenciaservice.dtos;

import com.example.msasistenciaservice.models.Asistencia;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaDTO {
    private Long asistenciaId;
    @NotNull
    private Long registroId;
    @NotNull
    private Long participantId;
    @NotNull
    private Long eventoId;
    private String codigoQr;
    private Asistencia.EstadoAsistencia estado;
    private LocalDateTime fechaEntrada;
    private String observaciones;
}
