package com.emm.mseventoservice.service;

import com.emm.mseventoservice.dtos.SessionDTO;

import java.util.List;

public interface SessionService {
    SessionDTO create(SessionDTO sessionDto);

    SessionDTO update(Long sessionId, SessionDTO sessionDto);

    void delete(Long sessionId);

    SessionDTO findById(Long sessionId);

    List<SessionDTO> findAll();

    List<SessionDTO> findByEventId(Long eventId);

    List<SessionDTO> findByEventIdOrderedByDateTime(Long eventId);

}
