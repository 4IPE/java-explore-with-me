package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import service.model.Location;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface LocationMapper {
    Location toEntity(ru.practicum.ewm.dto.event.Location location);
}
