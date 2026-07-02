package com.example.msregistroservice.controller;

import com.example.msregistroservice.dtos.RegistroDTO;
import com.example.msregistroservice.services.RegistroService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registros")
@RequiredArgsConstructor
public class RegistroController {

    private final RegistroService service;

    @PostMapping("/participant/{participantId}")
    public RegistroDTO crear(
            @PathVariable Long participantId,
            @RequestBody RegistroDTO dto
    ) {
        return service.guardar(participantId, dto);
    }

    @GetMapping("/{id}")
    public RegistroDTO obtener(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public List<RegistroDTO> listar() {
        return service.listar();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
