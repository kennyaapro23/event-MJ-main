package com.emm.mseventoservice.dtos;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionDTO {
    private Long sessionId;
    private Long eventId;
    private String title;
    private LocalDateTime dateTime;
    private Integer durationMinutes;
    private String speaker;
}