package com.example.mspagoservice.services.impl;

import com.example.mspagoservice.dtos.PagoDTO;
import com.example.mspagoservice.models.Pago;
import com.example.mspagoservice.repositorys.PagoRepository;
import com.example.mspagoservice.services.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;

    @Override
    public PagoDTO procesar(PagoDTO dto) {
        Pago pago = Pago.builder()
                .registroId(dto.getRegistroId())
                .participantId(dto.getParticipantId())
                .eventoId(dto.getEventoId())
                .monto(dto.getMonto())
                .metodoPago(dto.getMetodoPago())
                .estado(Pago.EstadoPago.PAGADO)
                .fechaPago(LocalDateTime.now())
                .referencia(dto.getReferencia())
                .build();
        return toDTO(pagoRepository.save(pago));
    }

    @Override
    public PagoDTO buscarPorId(Long id) {
        return pagoRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado: " + id));
    }

    @Override
    public List<PagoDTO> listarPorParticipante(Long participantId) {
        return pagoRepository.findByParticipantId(participantId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<PagoDTO> listarPorEvento(Long eventoId) {
        return pagoRepository.findByEventoId(eventoId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public PagoDTO actualizarEstado(Long id, String estado) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado: " + id));
        pago.setEstado(Pago.EstadoPago.valueOf(estado));
        return toDTO(pagoRepository.save(pago));
    }

    private PagoDTO toDTO(Pago pago) {
        return PagoDTO.builder()
                .pagoId(pago.getPagoId())
                .registroId(pago.getRegistroId())
                .participantId(pago.getParticipantId())
                .eventoId(pago.getEventoId())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .fechaPago(pago.getFechaPago())
                .referencia(pago.getReferencia())
                .build();
    }
}
