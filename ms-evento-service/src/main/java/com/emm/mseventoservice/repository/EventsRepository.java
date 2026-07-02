package com.emm.mseventoservice.repository;

import com.emm.mseventoservice.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventsRepository extends JpaRepository<Event, Long> {

    Page<Event> findByStatus(Event.EventStatus status, Pageable pageable);

    List<Event> findByOrganizerId(Long organizerId);

    Page<Event> findByOrganizerIdAndStatus(Long organizerId, Event.EventStatus status, Pageable pageable);

    Optional<Event> findByEventIdAndStatus(Long eventId, Event.EventStatus status);

    List<Event> findByModalityAndStatus(Event.Modality modality, Event.EventStatus status);

    @Query("SELECT e FROM Event e WHERE e.startDate >= :startDate AND e.endDate <= :endDate AND e.status = :status")
    List<Event> findEventsBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") Event.EventStatus status
    );

    @Query("SELECT e FROM Event e WHERE e.status = :status AND e.startDate >= :today ORDER BY e.startDate ASC")
    Page<Event> findUpcomingEvents(@Param("today") LocalDate today, @Param("status") Event.EventStatus status, Pageable pageable);

    boolean existsByEventIdAndOrganizerId(Long eventId, Long organizerId);
}