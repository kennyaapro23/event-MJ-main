package com.emm.mseventoservice.mappers;

import com.emm.mseventoservice.dtos.*;
import com.emm.mseventoservice.models.Event;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    public Event toEntity(EventCreateDTO dto) {
        if (dto == null) return null;

        return Event.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .modality(dto.getModality())
                .eventType(dto.getEventType())
                .maxCapacity(Optional.ofNullable(dto.getMaxCapacity()).orElse(0))
                .organizerId(dto.getOrganizerId())
                .address(dto.getAddress())
                .status(Event.EventStatus.ACTIVE)
                .price(dto.getPrice())
                .build();
    }

    public EventResponseDTO toResponseDTO(Event event) {
        if (event == null) return null;

        return EventResponseDTO.builder()
                .eventId(event.getEventId())
                .name(event.getName())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .modality(event.getModality())
                .eventType(event.getEventType())
                .maxCapacity(event.getMaxCapacity())
                .organizerId(event.getOrganizerId())
                .address(event.getAddress())
                .status(event.getStatus())
                .price(event.getPrice())
                .build();
    }

    public EventResponseDTO toResponseDTO(Event event, AuthUserDto organizer) {
        EventResponseDTO dto = toResponseDTO(event);
        if (dto != null && organizer != null) {
            dto.setOrganizer(organizer.getUserName());
        }

        return dto;
    }

    public EventSummaryDTO toSummaryDTO(Event event) {
        if (event == null) return null;

        return EventSummaryDTO.builder()
                .eventId(event.getEventId())
                .name(event.getName())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .modality(event.getModality())
                .eventType(event.getEventType())
                .maxCapacity(event.getMaxCapacity())
                .status(event.getStatus())
                .price(event.getPrice())
                .build();
    }

    public List<EventResponseDTO> toResponseDTOList(List<Event> events) {
        return mapList(events, this::toResponseDTO);
    }

    public List<EventSummaryDTO> toSummaryDTOList(List<Event> events) {
        return mapList(events, this::toSummaryDTO);
    }

    public void updateEntityFromDTO(Event event, EventUpdateDTO dto) {
        if (event == null || dto == null) return;

        Optional.ofNullable(dto.getName()).ifPresent(event::setName);
        Optional.ofNullable(dto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(dto.getStartDate()).ifPresent(event::setStartDate);
        Optional.ofNullable(dto.getEndDate()).ifPresent(event::setEndDate);
        Optional.ofNullable(dto.getModality()).ifPresent(event::setModality);
        Optional.ofNullable(dto.getEventType()).ifPresent(event::setEventType);
        Optional.ofNullable(dto.getMaxCapacity()).ifPresent(event::setMaxCapacity);
        Optional.ofNullable(dto.getAddress()).ifPresent(event::setAddress);
        Optional.ofNullable(dto.getStatus()).ifPresent(event::setStatus);
        Optional.ofNullable(dto.getPrice()).ifPresent(event::setPrice);

    }



    private <T, R> List<R> mapList(List<T> source, java.util.function.Function<T, R> mapper) {
        return Optional.ofNullable(source)
                .map(list -> list.stream().map(mapper).collect(Collectors.toList()))
                .orElse(List.of());
    }
}
