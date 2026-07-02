package com.example.msasistenciaservice.services.impl;

import com.example.msasistenciaservice.dtos.AsistenciaDTO;
import com.example.msasistenciaservice.exceptions.AsistenciaNotFoundException;
import com.example.msasistenciaservice.exceptions.ParticipantNotFoundException;
import com.example.msasistenciaservice.exceptions.QrNotFoundException;
import com.example.msasistenciaservice.feign.ParticipantFeign;
import com.example.msasistenciaservice.models.Asistencia;
import com.example.msasistenciaservice.repositorys.AsistenciaRepository;
import com.example.msasistenciaservice.services.AsistenciaService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final ParticipantFeign participantFeign;

    @Override
    public AsistenciaDTO registrar(AsistenciaDTO dto) {
        try {
            participantFeign.buscarPorId(dto.getParticipantId());
        } catch (FeignException.NotFound e) {
            throw new ParticipantNotFoundException("Participante no encontrado: " + dto.getParticipantId());
        }
        Asistencia asistencia = Asistencia.builder()
                .registroId(dto.getRegistroId())
                .participantId(dto.getParticipantId())
                .eventoId(dto.getEventoId())
                .codigoQr(UUID.randomUUID().toString())
                .estado(Asistencia.EstadoAsistencia.AUSENTE)
                .observaciones(dto.getObservaciones())
                .build();
        return toDTO(asistenciaRepository.save(asistencia));
    }

    @Override
    public AsistenciaDTO confirmarPorQr(String codigoQr) {
        Asistencia asistencia = asistenciaRepository.findByCodigoQr(codigoQr)
                .orElseThrow(() -> new QrNotFoundException("QR no encontrado: " + codigoQr));
        if (asistencia.getEstado() == Asistencia.EstadoAsistencia.PRESENTE) {
            return toDTO(asistencia);
        }
        asistencia.setEstado(Asistencia.EstadoAsistencia.PRESENTE);
        asistencia.setFechaEntrada(LocalDateTime.now());
        return toDTO(asistenciaRepository.save(asistencia));
    }

    @Override
    public AsistenciaDTO buscarPorId(Long id) {
        return asistenciaRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new AsistenciaNotFoundException("Asistencia no encontrada: " + id));
    }

    @Override
    public List<AsistenciaDTO> listarPorEvento(Long eventoId) {
        return asistenciaRepository.findByEventoId(eventoId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AsistenciaDTO> listarPorParticipante(Long participantId) {
        return asistenciaRepository.findByParticipantId(participantId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AsistenciaDTO toDTO(Asistencia a) {
        return AsistenciaDTO.builder()
                .asistenciaId(a.getAsistenciaId())
                .registroId(a.getRegistroId())
                .participantId(a.getParticipantId())
                .eventoId(a.getEventoId())
                .codigoQr(a.getCodigoQr())
                .estado(a.getEstado())
                .fechaEntrada(a.getFechaEntrada())
                .observaciones(a.getObservaciones())
                .build();
    }
}
