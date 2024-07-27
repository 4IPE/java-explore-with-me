package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.EndpointHitOutDto;
import ru.practicum.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.dto.EndpointHitOutDto(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startFormat AND :endFormat " +
            "AND (eh.uri IN :uris) " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC")
    List<EndpointHitOutDto> findAllEndpointHits(@Param("startFormat") LocalDateTime startFormat,
                                                @Param("endFormat") LocalDateTime endFormat,
                                                @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.dto.EndpointHitOutDto(eh.app, eh.uri, COUNT(eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startFormat AND :endFormat " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh.ip) DESC")
    List<EndpointHitOutDto> findAllEndpointHitsWithoutUris(@Param("startFormat") LocalDateTime startFormat,
                                                           @Param("endFormat") LocalDateTime endFormat);

    @Query("SELECT new ru.practicum.dto.EndpointHitOutDto(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startFormat AND :endFormat " +
            "AND (eh.uri IN :uris) " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    List<EndpointHitOutDto> findUniqueEndpointHits(@Param("startFormat") LocalDateTime startFormat,
                                                   @Param("endFormat") LocalDateTime endFormat,
                                                   @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.dto.EndpointHitOutDto(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.timestamp BETWEEN :startFormat AND :endFormat " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    List<EndpointHitOutDto> findUniqueEndpointHitsWithoutUris(@Param("startFormat") LocalDateTime startFormat,
                                                              @Param("endFormat") LocalDateTime endFormat);
}
