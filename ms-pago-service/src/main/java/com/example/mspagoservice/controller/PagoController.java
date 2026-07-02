package com.example.mspagoservice.controller;

import com.example.mspagoservice.dtos.PagoDTO;
import com.example.mspagoservice.services.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public PagoDTO procesar(@RequestBody PagoDTO dto) {
        return pagoService.procesar(dto);
    }

    @GetMapping("/{id}")
    public PagoDTO obtener(@PathVariable Long id) {
        return pagoService.buscarPorId(id);
    }

    @GetMapping("/participante/{participantId}")
    public List<PagoDTO> porParticipante(@PathVariable Long participantId) {
        return pagoService.listarPorParticipante(participantId);
    }

    @GetMapping("/evento/{eventoId}")
    public List<PagoDTO> porEvento(@PathVariable Long eventoId) {
        return pagoService.listarPorEvento(eventoId);
    }

    @PatchMapping("/{id}/estado")
    public PagoDTO actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return pagoService.actualizarEstado(id, estado);
    }
}
