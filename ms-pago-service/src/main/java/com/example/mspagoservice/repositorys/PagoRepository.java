package com.example.mspagoservice.repositorys;

import com.example.mspagoservice.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByParticipantId(Long participantId);
    List<Pago> findByEventoId(Long eventoId);
    List<Pago> findByRegistroId(Long registroId);
}
