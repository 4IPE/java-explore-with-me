package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import service.dto.event.LocationDto;
import service.model.Location;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface LocationMapper {
    Location toLocation(LocationDto locationDto);

    LocationDto toLocationDto(Location location);
}
