package ru.practicum.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT new ru.practicum.model.Stats(hits.app, hits.uri, COUNT(hits.ip)) "
            + "FROM Hit AS hits "
            + "WHERE hits.uri IN :uris AND (hits.timestamp >= :start AND hits.timestamp <= :end ) "
            + "GROUP BY hits.app, hits.uri ")
    List<Stats> getAllByUriNotUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                            @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.model.Stats(hits.app, hits.uri, COUNT(DISTINCT hits.ip)) "
            + "FROM Hit AS hits "
            + "WHERE hits.uri IN :uris AND (hits.timestamp >= :start AND hits.timestamp <= :end ) "
            + "GROUP BY hits.app, hits.uri ")
    List<Stats> getAllByUriAndUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                       @Param("uris") List<String> uris);
}