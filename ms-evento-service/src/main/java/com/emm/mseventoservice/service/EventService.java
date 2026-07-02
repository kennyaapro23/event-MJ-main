package com.emm.mseventoservice.service;

import com.emm.mseventoservice.dtos.EventCreateDTO;
import com.emm.mseventoservice.dtos.EventResponseDTO;
import com.emm.mseventoservice.dtos.EventSummaryDTO;
import com.emm.mseventoservice.dtos.EventUpdateDTO;
import com.emm.mseventoservice.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    EventResponseDTO createEvent(EventCreateDTO createDTO);

    EventResponseDTO updateEvent(Long eventId, EventUpdateDTO updateDTO, Long organizerId);

    EventResponseDTO getEventById(Long eventId);

    EventResponseDTO getEventByIdWithOrganizer(Long eventId);

    Page<EventResponseDTO> getAllEvents(Pageable pageable);

    Page<EventResponseDTO> getEventsByStatus(Event.EventStatus status, Pageable pageable);

    List<EventResponseDTO> getEventsByOrganizerId(Long organizerId);

    Page<EventResponseDTO> getEventsByOrganizerIdAndStatus(Long organizerId, Event.EventStatus status, Pageable pageable);

    List<EventSummaryDTO> getEventsByModality(Event.Modality modality);

    List<EventSummaryDTO> getEventsBetweenDates(LocalDate startDate, LocalDate endDate);

    Page<EventSummaryDTO> getUpcomingEvents(Pageable pageable);

    void deleteEvent(Long eventId, Long organizerId);

    EventResponseDTO changeEventStatus(Long eventId, Event.EventStatus status, Long organizerId);

    boolean isOrganizerOwner(Long eventId, Long organizerId);
}