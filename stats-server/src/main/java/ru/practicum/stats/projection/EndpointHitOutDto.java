package ru.practicum.stats.projection;

import org.springframework.data.rest.core.config.Projection;
import ru.practicum.stats.model.EndpointHit;


@Projection(name = "EndpointHitProjection", types = EndpointHit.class)
public interface EndpointHitOutDto {
    String getApp();

    String getUri();

    Long getHits();
}
