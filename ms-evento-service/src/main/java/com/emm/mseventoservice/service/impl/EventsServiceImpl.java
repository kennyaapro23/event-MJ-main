package com.emm.mseventoservice.service.impl;


import com.emm.mseventoservice.dtos.*;
import com.emm.mseventoservice.exceptions.*;
import com.emm.mseventoservice.feign.OrganizerFeignClient;
import com.emm.mseventoservice.mappers.EventMapper;
import com.emm.mseventoservice.models.Event;
import com.emm.mseventoservice.repository.EventsRepository;
import com.emm.mseventoservice.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventService {

    private final EventsRepository eventRepository;
    private final EventMapper eventMapper;
    private final OrganizerFeignClient organizerFeignClient;

    @Override
    @Transactional
    public EventResponseDTO createEvent(EventCreateDTO createDTO) {
        log.info("Creating event: {}", createDTO.getName());

        // Validar fechas
        validateEventDates(createDTO.getStartDate(), createDTO.getEndDate());

        // Validar que el organizador existe
        Boolean organizerExists = organizerFeignClient.existsById(createDTO.getOrganizerId());
        if (organizerExists == null || !organizerExists) {
            throw new OrganizerNotFoundException(createDTO.getOrganizerId());
        }

        // Obtener datos del organizador para confirmar que llegan
        AuthUserDto organizer = organizerFeignClient.getUserById(createDTO.getOrganizerId());
        log.info("Organizer obtenido desde ms-auth: {}", organizer);

        // Validar capacidad
        if (createDTO.getMaxCapacity() != null && createDTO.getMaxCapacity() < 0) {
            throw new InvalidEventCapacityException("Max capacity cannot be negative");
        }

        // Guardar evento
        Event event = eventMapper.toEntity(createDTO);
        Event savedEvent = eventRepository.save(event);

        log.info("Event created successfully with ID: {}", savedEvent.getEventId());

        // Mapea incluyendo el organizer
        return eventMapper.toResponseDTO(savedEvent, organizer);
    }

    @Override
    @Transactional
    public EventResponseDTO updateEvent(Long eventId, EventUpdateDTO updateDTO, Long organizerId) {
        log.info("Updating event with ID: {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        // Verificar que el organizador es el dueño del evento
        if (!event.getOrganizerId().equals(organizerId)) {
            throw new UnauthorizedEventAccessException(eventId, organizerId);
        }

        // Validar fechas si se actualizan
        LocalDate newStartDate = updateDTO.getStartDate() != null ? updateDTO.getStartDate() : event.getStartDate();
        LocalDate newEndDate = updateDTO.getEndDate() != null ? updateDTO.getEndDate() : event.getEndDate();
        validateEventDates(newStartDate, newEndDate);

        // Validar capacidad
        if (updateDTO.getMaxCapacity() != null && updateDTO.getMaxCapacity() < 0) {
            throw new InvalidEventCapacityException("Max capacity cannot be negative");
        }

        eventMapper.updateEntityFromDTO(event, updateDTO);
        Event updatedEvent = eventRepository.save(event);

        log.info("Event updated successfully with ID: {}", eventId);
        return eventMapper.toResponseDTO(updatedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDTO getEventById(Long eventId) {
        log.info("Retrieving event with ID: {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        return eventMapper.toResponseDTO(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDTO getEventByIdWithOrganizer(Long eventId) {
        log.info("Retrieving event with organizer info for ID: {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        AuthUserDto organizer = organizerFeignClient.getUserById(event.getOrganizerId());

        return eventMapper.toResponseDTO(event, organizer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getAllEvents(Pageable pageable) {
        log.info("Retrieving all events");

        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(eventMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEventsByStatus(Event.EventStatus status, Pageable pageable) {
        log.info("Retrieving events with status: {}", status);

        Page<Event> events = eventRepository.findByStatus(status, pageable);
        return events.map(eventMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDTO> getEventsByOrganizerId(Long organizerId) {
        log.info("Retrieving events for organizer ID: {}", organizerId);

        List<Event> events = eventRepository.findByOrganizerId(organizerId);
        return eventMapper.toResponseDTOList(events);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEventsByOrganizerIdAndStatus(Long organizerId, Event.EventStatus status, Pageable pageable) {
        log.info("Retrieving events for organizer ID: {} with status: {}", organizerId, status);

        Page<Event> events = eventRepository.findByOrganizerIdAndStatus(organizerId, status, pageable);
        return events.map(eventMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventSummaryDTO> getEventsByModality(Event.Modality modality) {
        log.info("Retrieving events with modality: {}", modality);

        List<Event> events = eventRepository.findByModalityAndStatus(modality, Event.EventStatus.ACTIVE);
        return eventMapper.toSummaryDTOList(events);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventSummaryDTO> getEventsBetweenDates(LocalDate startDate, LocalDate endDate) {
        log.info("Retrieving events between {} and {}", startDate, endDate);

        if (startDate.isAfter(endDate)) {
            throw new InvalidEventDateException("Start date must be before end date");
        }

        List<Event> events = eventRepository.findEventsBetweenDates(startDate, endDate, Event.EventStatus.ACTIVE);
        return eventMapper.toSummaryDTOList(events);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventSummaryDTO> getUpcomingEvents(Pageable pageable) {
        log.info("Retrieving upcoming events");

        Page<Event> events = eventRepository.findUpcomingEvents(LocalDate.now(), Event.EventStatus.ACTIVE, pageable);
        return events.map(eventMapper::toSummaryDTO);
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId, Long organizerId) {
        log.info("Deleting event with ID: {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        // Verificar que el organizador es el dueño del evento
        if (!event.getOrganizerId().equals(organizerId)) {
            throw new UnauthorizedEventAccessException(eventId, organizerId);
        }

        eventRepository.delete(event);
        log.info("Event deleted successfully with ID: {}", eventId);
    }

    @Override
    @Transactional
    public EventResponseDTO changeEventStatus(Long eventId, Event.EventStatus status, Long organizerId) {
        log.info("Changing status of event {} to {}", eventId, status);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        // Verificar que el organizador es el dueño del evento
        if (!event.getOrganizerId().equals(organizerId)) {
            throw new UnauthorizedEventAccessException(eventId, organizerId);
        }

        event.setStatus(status);
        Event updatedEvent = eventRepository.save(event);

        log.info("Event status changed successfully");
        return eventMapper.toResponseDTO(updatedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOrganizerOwner(Long eventId, Long organizerId) {
        return eventRepository.existsByEventIdAndOrganizerId(eventId, organizerId);
    }

    private void validateEventDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidEventDateException("Start date must be before or equal to end date");
        }

        if (endDate.isBefore(LocalDate.now())) {
            throw new InvalidEventDateException("End date cannot be in the past");
        }
    }
}