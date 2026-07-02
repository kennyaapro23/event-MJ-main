package com.emm.mseventoservice.mappers;

import com.emm.mseventoservice.dtos.SessionDTO;
import com.emm.mseventoservice.models.Session;
import com.emm.mseventoservice.models.Event;


import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public SessionDTO toDto(Session session) {
        if (session == null) {
            return null;
        }

        return SessionDTO.builder()
                .sessionId(session.getSessionId())
                .eventId(session.getEvent() != null ? session.getEvent().getEventId() : null)
                .title(session.getTitle())
                .dateTime(session.getDateTime())
                .durationMinutes(session.getDurationMinutes())
                .speaker(session.getSpeaker())
                .build();
    }

    public Session toEntity(SessionDTO dto, Event event) {
        if (dto == null) {
            return null;
        }

        return Session.builder()
                .sessionId(dto.getSessionId())
                .event(event)
                .title(dto.getTitle())
                .dateTime(dto.getDateTime())
                .durationMinutes(dto.getDurationMinutes())
                .speaker(dto.getSpeaker())
                .build();
    }
}