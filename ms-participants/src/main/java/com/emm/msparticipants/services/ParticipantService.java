package com.emm.msparticipants.services;

import com.emm.msparticipants.dtos.CreateParticipantDTO;
import com.emm.msparticipants.dtos.ParticipantDTO;

import java.util.List;

public interface ParticipantService {
    ParticipantDTO createParticipant(CreateParticipantDTO dto);
    List<ParticipantDTO> getAllParticipants();
    ParticipantDTO getParticipantById(Long id);
    void deleteParticipant(Long id);
    ParticipantDTO updateParticipant(Long id, CreateParticipantDTO dto);



}
