package com.emm.mseventoservice.dtos;

import com.emm.mseventoservice.models.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventUpdateDTO {
    
    @Size(max = 150, message = "Event name must not exceed 150 characters")
    private String name;
    
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    private Event.Modality modality;
    
    private Event.EventType eventType;
    
    @Min(value = 0, message = "Max capacity must be at least 0")
    private Integer maxCapacity;
    
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
    
    private Event.EventStatus status;
    private BigDecimal price;
}
