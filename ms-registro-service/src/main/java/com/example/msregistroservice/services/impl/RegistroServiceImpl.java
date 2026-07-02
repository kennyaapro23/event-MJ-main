package com.example.msregistroservice.services.impl;

import com.example.msregistroservice.dtos.ParticipantDTO;
import com.example.msregistroservice.dtos.RegistroDTO;

import com.example.msregistroservice.feign.ParticipantFeign;
import com.example.msregistroservice.models.Registro;
import com.example.msregistroservice.repositorys.RegistroRepository;
import com.example.msregistroservice.services.RegistroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistroServiceImpl implements RegistroService {

    private final RegistroRepository repository;
    private final ParticipantFeign participantFeign;

    @Override
    public RegistroDTO guardar(Long participantId, RegistroDTO dto) {

        Registro registro = Registro.builder()
                .timestamp(dto.getTimestamp())
                .observations(dto.getObservations())
                .idEvento(dto.getIdEvento())
                .participantId(participantId)
                .build();

        Registro saved = repository.save(registro);

        ParticipantDTO participant = participantFeign.buscarPorId(participantId);
        System.out.println("participant = " + participant.toString());
        return mapToDTO(saved, participant);
    }

    @Override
    public RegistroDTO buscarPorId(Long id) {

        Registro registro = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        ParticipantDTO participant = participantFeign.buscarPorId(registro.getParticipantId());

        return mapToDTO(registro, participant);
    }

    @Override
    public List<RegistroDTO> listar() {
        return repository.findAll()
                .stream()
                .map(registro -> {
                    ParticipantDTO participant =
                            participantFeign.buscarPorId(registro.getParticipantId());
                    return mapToDTO(registro, participant);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    private RegistroDTO mapToDTO(Registro registro, ParticipantDTO participant) {
        return RegistroDTO.builder()
                .idAttendance(registro.getIdAttendance())
                .timestamp(registro.getTimestamp())
                .observations(registro.getObservations())
                .idEvento(registro.getIdEvento())
                .participantDTO(participant)
                .build();
    }
}
