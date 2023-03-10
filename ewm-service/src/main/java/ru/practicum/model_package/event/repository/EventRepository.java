package ru.practicum.model_package.event.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.model_package.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findByInitiatorId(Long initiatorId, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE e.id IN :events")
    List<Event> getAllByEvents(List<Long> events);

    Long countByCategoryId(Long id);
}