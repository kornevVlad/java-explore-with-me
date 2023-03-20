package ru.practicum.modelpackage.participation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.modelpackage.event.model.Event;
import ru.practicum.modelpackage.participation.model.ParticipationRequest;
import ru.practicum.modelpackage.participation.status_request.StatusRequest;
import ru.practicum.modelpackage.user.model.User;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findParticipationRequestByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    @Query("SELECT count(ev) FROM ParticipationRequest ev WHERE ev.event.id = :event_id AND " +
            "ev.status = 'CONFIRMED'")
    Long countConfirmedByEventId(@Param("event_id")Long eventId);

    Long countByEventAndStatus(Event event, StatusRequest status);

    ParticipationRequest findParticipationRequestByRequesterAndStatus(User requester, StatusRequest status);
}
