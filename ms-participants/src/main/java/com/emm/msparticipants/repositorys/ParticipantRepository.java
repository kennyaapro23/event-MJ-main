package com.emm.msparticipants.repositorys;

import com.emm.msparticipants.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant,Long> {
    Optional<Participant> findByEmail(String email);
}
