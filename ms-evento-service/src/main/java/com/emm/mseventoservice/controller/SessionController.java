package com.emm.mseventoservice.controller;

import com.emm.mseventoservice.dtos.SessionDTO;
import com.emm.mseventoservice.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    // Crear sesión - Solo ORGANIZER y ADMIN
    @PostMapping
    public ResponseEntity<SessionDTO> create(
            @RequestBody SessionDTO sessionDto,
            @RequestHeader("X-User-Roles") String roles) {

        if (!roles.contains("ROLE_ORGANIZER") && !roles.contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        SessionDTO created = sessionService.create(sessionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Actualizar sesión - Solo ORGANIZER y ADMIN
    @PutMapping("/{sessionId}")
    public ResponseEntity<SessionDTO> update(
            @PathVariable Long sessionId,
            @RequestBody SessionDTO sessionDto,
            @RequestHeader("X-User-Roles") String roles) {

        if (!roles.contains("ROLE_ORGANIZER") && !roles.contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        SessionDTO updated = sessionService.update(sessionId, sessionDto);
        return ResponseEntity.ok(updated);
    }

    // Eliminar sesión - Solo ORGANIZER y ADMIN
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long sessionId,
            @RequestHeader("X-User-Roles") String roles) {

        if (!roles.contains("ROLE_ORGANIZER") && !roles.contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        sessionService.delete(sessionId);
        return ResponseEntity.noContent().build();
    }

    // Ver sesión - Todos pueden
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionDTO> findById(@PathVariable Long sessionId) {
        SessionDTO session = sessionService.findById(sessionId);
        return ResponseEntity.ok(session);
    }

    // Listar todas las sesiones - Todos pueden
    @GetMapping
    public ResponseEntity<List<SessionDTO>> findAll() {
        List<SessionDTO> sessions = sessionService.findAll();
        return ResponseEntity.ok(sessions);
    }

    // Listar sesiones de un evento - Todos pueden
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<SessionDTO>> findByEventId(@PathVariable Long eventId) {
        List<SessionDTO> sessions = sessionService.findByEventId(eventId);
        return ResponseEntity.ok(sessions);
    }

    // Listar sesiones de un evento ordenadas por fecha - Todos pueden
    @GetMapping("/event/{eventId}/ordered")
    public ResponseEntity<List<SessionDTO>> findByEventIdOrdered(@PathVariable Long eventId) {
        List<SessionDTO> sessions = sessionService.findByEventIdOrderedByDateTime(eventId);
        return ResponseEntity.ok(sessions);
    }
}