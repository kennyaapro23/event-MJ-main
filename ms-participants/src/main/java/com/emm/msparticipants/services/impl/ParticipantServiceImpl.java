package com.emm.msparticipants.services.impl;

import com.emm.msparticipants.dtos.CreateParticipantDTO;
import com.emm.msparticipants.dtos.ParticipantDTO;
import com.emm.msparticipants.exceptions.DuplicateEmailException;
import com.emm.msparticipants.exceptions.ResourceNotFoundException;
import com.emm.msparticipants.mappers.ParticipantMapper;
import com.emm.msparticipants.models.Participant;
import com.emm.msparticipants.repositorys.ParticipantRepository;
import com.emm.msparticipants.services.ParticipantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;
    @Override
    @Transactional
    public ParticipantDTO createParticipant(CreateParticipantDTO dto) {

        if (participantRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Participant already exists with email: " + dto.getEmail());
        }

        Participant participant = participantMapper.toEntityFromCreateDto(dto);
        Participant savedParticipant = participantRepository.save(participant);

        return participantMapper.toDto(savedParticipant);
    }

    @Override
    public List<ParticipantDTO> getAllParticipants() {

        return participantRepository.findAll()
                .stream()
                .map(participantMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public ParticipantDTO getParticipantById(Long id) {

        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));

        return participantMapper.toDto(participant);
    }

    @Override
    @Transactional
    public ParticipantDTO updateParticipant(Long id, CreateParticipantDTO dto) {

        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));

        if (!dto.getEmail().equals(participant.getEmail())
                && participantRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Participant already exists with email: " + dto.getEmail());
        }

        participantMapper.updateParticipantFromDto(dto, participant);
        Participant updatedParticipant = participantRepository.save(participant);

        return participantMapper.toDto(updatedParticipant);
    }

    @Override
    @Transactional
    public void deleteParticipant(Long id) {

        if (!participantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Participant not found with id: " + id);
        }

        participantRepository.deleteById(id);
    }
}
