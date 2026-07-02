package com.emm.mseventoservice.dtos;

import com.emm.mseventoservice.models.Event;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponseDTO {
    
    private Long eventId;
    private String name;
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    private Event.Modality modality;
    private Event.EventType eventType;
    private Integer maxCapacity;
    private Long organizerId;
    private String  organizer;
    private String address;
    private Event.EventStatus status;

    private BigDecimal price;
}