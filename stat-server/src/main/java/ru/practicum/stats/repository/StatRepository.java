package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stats.model.EndpointHit;

import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {
    List<EndpointHit> findByUri(String uri);
}
