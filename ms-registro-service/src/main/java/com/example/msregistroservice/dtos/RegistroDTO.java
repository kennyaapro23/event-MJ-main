package com.example.msregistroservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {
    private Long idAttendance;
    private LocalDateTime timestamp;
    private String observations;
    private String idEvento;
    private ParticipantDTO participantDTO;
}
