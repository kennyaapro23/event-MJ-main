package com.example.msregistroservice.services;

import com.example.msregistroservice.dtos.RegistroDTO;

import java.util.List;

public interface RegistroService {

    RegistroDTO guardar(Long participantId, RegistroDTO registroDTO);

    RegistroDTO buscarPorId(Long id);

    List<RegistroDTO> listar();

    void eliminar(Long id);
}
