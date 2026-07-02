package com.emm.mseventoservice.service.impl;

import com.emm.mseventoservice.dtos.SessionDTO;
import com.emm.mseventoservice.exceptions.DuplicateSessionException;
import com.emm.mseventoservice.exceptions.EventNotFoundException;
import com.emm.mseventoservice.exceptions.SessionNotFoundException;
import com.emm.mseventoservice.mappers.SessionMapper;
import com.emm.mseventoservice.models.Event;
import com.emm.mseventoservice.models.Session;
import com.emm.mseventoservice.repository.EventsRepository;
import com.emm.mseventoservice.repository.SessionRepository;
import com.emm.mseventoservice.service.SessionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private final EventsRepository eventRepository;

    private final SessionMapper sessionMapper;

    @Override
    public SessionDTO create(SessionDTO sessionDto) {
        // Valida que el evento exista
        Event event = eventRepository.findById(sessionDto.getEventId())
                .orElseThrow(() -> new EventNotFoundException(sessionDto.getEventId()));

        // Verifica duplicados (mismo título en el mismo evento)
        if (sessionRepository.existsByEvent_EventIdAndTitle(sessionDto.getEventId(), sessionDto.getTitle())) {
            throw new DuplicateSessionException("Ya existe una sesión con el título '" + sessionDto.getTitle() + "' en este evento");
        }

        Session session = sessionMapper.toEntity(sessionDto, event);
        Session savedSession = sessionRepository.save(session);

        return sessionMapper.toDto(savedSession);
    }

    @Override
    public SessionDTO update(Long sessionId, SessionDTO sessionDto) {
        Session existingSession = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        // Valida evento si cambió
        if (sessionDto.getEventId() != null &&
                !existingSession.getEvent().getEventId().equals(sessionDto.getEventId())) {

            Event newEvent = eventRepository.findById(sessionDto.getEventId())
                    .orElseThrow(() -> new EventNotFoundException(sessionDto.getEventId()));
            existingSession.setEvent(newEvent);
        }

        // Actualiza campos
        if (sessionDto.getTitle() != null) existingSession.setTitle(sessionDto.getTitle());
        if (sessionDto.getDateTime() != null) existingSession.setDateTime(sessionDto.getDateTime());
        if (sessionDto.getDurationMinutes() != null) existingSession.setDurationMinutes(sessionDto.getDurationMinutes());
        if (sessionDto.getSpeaker() != null) existingSession.setSpeaker(sessionDto.getSpeaker());

        Session updatedSession = sessionRepository.save(existingSession);
        return sessionMapper.toDto(updatedSession);
    }

    @Override
    public void delete(Long sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new SessionNotFoundException(sessionId);
        }
        sessionRepository.deleteById(sessionId);
    }

    @Override
    public SessionDTO findById(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
        return sessionMapper.toDto(session);
    }

    @Override
    public List<SessionDTO> findAll() {
        return sessionRepository.findAll().stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SessionDTO> findByEventId(Long eventId) {
        // Valida que el evento exista
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }

        return sessionRepository.findByEvent_EventId(eventId).stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SessionDTO> findByEventIdOrderedByDateTime(Long eventId) {
        // Valida que el evento exista
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }

        return sessionRepository.findByEvent_EventIdOrderByDateTimeAsc(eventId).stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }
}