package com.example.msasistenciaservice.repositorys;

import com.example.msasistenciaservice.models.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    Optional<Asistencia> findByCodigoQr(String codigoQr);
    List<Asistencia> findByEventoId(Long eventoId);
    List<Asistencia> findByParticipantId(Long participantId);
}
