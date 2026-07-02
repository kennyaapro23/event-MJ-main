package com.emm.msparticipants.mappers;

import com.emm.msparticipants.dtos.CreateParticipantDTO;
import com.emm.msparticipants.dtos.ParticipantDTO;
import com.emm.msparticipants.models.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface ParticipantMapper {

    @Mapping(target = "participantId", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    void updateParticipantFromDto(CreateParticipantDTO dto, @MappingTarget Participant participant);

    ParticipantDTO toDto(Participant participant);

    @Mapping(target = "participantId", ignore = true)
    @Mapping(target = "registrationDate", expression = "java(LocalDateTime.now())")
    Participant toEntityFromCreateDto(CreateParticipantDTO createDto);
}