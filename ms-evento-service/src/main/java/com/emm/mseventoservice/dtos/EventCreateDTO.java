package com.emm.mseventoservice.dtos;

import com.emm.mseventoservice.models.Event;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
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
public class EventCreateDTO {

    @NotBlank(message = "Event name is required")
    @Size(max = 150, message = "Event name must not exceed 150 characters")
    private String name;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "Modality is required")
    private Event.Modality modality;

    @NotNull(message = "Event type is required")
    private Event.EventType eventType;

    @Min(value = 0, message = "Max capacity must be at least 0")
    private Integer maxCapacity = 0;

    @NotNull(message = "Organizer ID is required")
    private Long organizerId;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
    @Column(precision = 10, scale = 2)

    private BigDecimal price = BigDecimal.ZERO; // ← NUEVO

}