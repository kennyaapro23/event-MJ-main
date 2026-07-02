package com.example.mspagoservice.services;

import com.example.mspagoservice.dtos.PagoDTO;
import java.util.List;

public interface PagoService {
    PagoDTO procesar(PagoDTO dto);
    PagoDTO buscarPorId(Long id);
    List<PagoDTO> listarPorParticipante(Long participantId);
    List<PagoDTO> listarPorEvento(Long eventoId);
    PagoDTO actualizarEstado(Long id, String estado);
}
