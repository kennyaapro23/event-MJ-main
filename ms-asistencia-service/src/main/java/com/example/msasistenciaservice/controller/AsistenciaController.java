package com.example.msasistenciaservice.controller;

import com.example.msasistenciaservice.dtos.AsistenciaDTO;
import com.example.msasistenciaservice.services.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @PostMapping
    public AsistenciaDTO registrar(@Valid @RequestBody AsistenciaDTO dto) {
        return asistenciaService.registrar(dto);
    }

    @PatchMapping("/qr/{codigoQr}")
    public AsistenciaDTO confirmarPorQr(@PathVariable String codigoQr) {
        return asistenciaService.confirmarPorQr(codigoQr);
    }

    @GetMapping("/{id}")
    public AsistenciaDTO obtener(@PathVariable Long id) {
        return asistenciaService.buscarPorId(id);
    }

    @GetMapping("/evento/{eventoId}")
    public List<AsistenciaDTO> porEvento(@PathVariable Long eventoId) {
        return asistenciaService.listarPorEvento(eventoId);
    }

    @GetMapping("/participante/{participantId}")
    public List<AsistenciaDTO> porParticipante(@PathVariable Long participantId) {
        return asistenciaService.listarPorParticipante(participantId);
    }
}
