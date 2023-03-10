package ru.practicum.model_package.participation_request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model_package.event.model.Event;
import ru.practicum.model_package.participation_request.model.ParticipationRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findParticipationRequestByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    @Query("SELECT count(ev) FROM ParticipationRequest ev WHERE ev.status LIKE 'CONFIRMED'" +
            " AND ev.event.id = :event_id ")
    Long countConfirmedByEventId(@Param("event_id")Long eventId);

    /*@Query("SELECT count(ev) FROM ParticipationRequest ev WHERE ev.status LIKE 'PENDING'" +
            " AND ev.event.id = :event_id ")*/
    Long countByEventAndStatus(Event event, String status);

    @Query("SELECT count(ev) FROM ParticipationRequest ev WHERE  ev.event.id = :event_id ")
    Long countLim(@Param("event_id") Long eventId);
}
