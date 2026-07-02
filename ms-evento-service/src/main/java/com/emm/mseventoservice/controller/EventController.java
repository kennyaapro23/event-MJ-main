package com.emm.mseventoservice.controller;


import com.emm.mseventoservice.dtos.EventCreateDTO;
import com.emm.mseventoservice.dtos.EventResponseDTO;
import com.emm.mseventoservice.dtos.EventSummaryDTO;
import com.emm.mseventoservice.dtos.EventUpdateDTO;
import com.emm.mseventoservice.models.Event;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.emm.mseventoservice.service.EventService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventCreateDTO createDTO) {
        log.info("REST request to create event: {}", createDTO.getName());
        EventResponseDTO response = eventService.createEvent(createDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody EventUpdateDTO updateDTO,
            @RequestHeader("X-Organizer-Id") Long organizerId) {
        log.info("REST request to update event: {}", eventId);
        EventResponseDTO response = eventService.updateEvent(eventId, updateDTO, organizerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long eventId) {
        log.info("REST request to get event: {}", eventId);
        EventResponseDTO response = eventService.getEventById(eventId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}/with-organizer")
    public ResponseEntity<EventResponseDTO> getEventByIdWithOrganizer(@PathVariable Long eventId) {
        log.info("REST request to get event with organizer info: {}", eventId);
        EventResponseDTO response = eventService.getEventByIdWithOrganizer(eventId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        log.info("REST request to get all events - page: {}, size: {}", page, size);

        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EventResponseDTO> response = eventService.getAllEvents(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByStatus(
            @PathVariable Event.EventStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("REST request to get events by status: {}", status);

        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").ascending());
        Page<EventResponseDTO> response = eventService.getEventsByStatus(status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<EventResponseDTO>> getEventsByOrganizerId(@PathVariable Long organizerId) {
        log.info("REST request to get events by organizer: {}", organizerId);
        List<EventResponseDTO> response = eventService.getEventsByOrganizerId(organizerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organizer/{organizerId}/status/{status}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByOrganizerIdAndStatus(
            @PathVariable Long organizerId,
            @PathVariable Event.EventStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("REST request to get events by organizer: {} and status: {}", organizerId, status);

        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").ascending());
        Page<EventResponseDTO> response = eventService.getEventsByOrganizerIdAndStatus(organizerId, status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/modality/{modality}")
    public ResponseEntity<List<EventSummaryDTO>> getEventsByModality(@PathVariable Event.Modality modality) {
        log.info("REST request to get events by modality: {}", modality);
        List<EventSummaryDTO> response = eventService.getEventsByModality(modality);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/between-dates")
    public ResponseEntity<List<EventSummaryDTO>> getEventsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to get events between {} and {}", startDate, endDate);
        List<EventSummaryDTO> response = eventService.getEventsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<EventSummaryDTO>> getUpcomingEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("REST request to get upcoming events");

        Pageable pageable = PageRequest.of(page, size);
        Page<EventSummaryDTO> response = eventService.getUpcomingEvents(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long eventId,
            @RequestHeader("X-Organizer-Id") Long organizerId) {
        log.info("REST request to delete event: {}", eventId);
        eventService.deleteEvent(eventId, organizerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{eventId}/status")
    public ResponseEntity<EventResponseDTO> changeEventStatus(
            @PathVariable Long eventId,
            @RequestParam Event.EventStatus status,
            @RequestHeader("X-Organizer-Id") Long organizerId) {
        log.info("REST request to change event status: {} to {}", eventId, status);
        EventResponseDTO response = eventService.changeEventStatus(eventId, status, organizerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}/is-owner")
    public ResponseEntity<Boolean> isOrganizerOwner(
            @PathVariable Long eventId,
            @RequestParam Long organizerId) {
        log.info("REST request to check if organizer {} owns event {}", organizerId, eventId);
        boolean isOwner = eventService.isOrganizerOwner(eventId, organizerId);
        return ResponseEntity.ok(isOwner);
    }
}