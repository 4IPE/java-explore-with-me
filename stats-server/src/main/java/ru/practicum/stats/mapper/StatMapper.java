package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.dto.EndpointHitInDto;
import ru.practicum.stats.model.EndpointHit;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface StatMapper {
    EndpointHit toEndpointHit(EndpointHitInDto endHit);
}
