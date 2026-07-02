package com.example.msasistenciaservice.services;

import com.example.msasistenciaservice.dtos.AsistenciaDTO;
import java.util.List;

public interface AsistenciaService {
    AsistenciaDTO registrar(AsistenciaDTO dto);
    AsistenciaDTO confirmarPorQr(String codigoQr);
    AsistenciaDTO buscarPorId(Long id);
    List<AsistenciaDTO> listarPorEvento(Long eventoId);
    List<AsistenciaDTO> listarPorParticipante(Long participantId);
}
