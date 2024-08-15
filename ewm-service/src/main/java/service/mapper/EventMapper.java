package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import service.dto.event.EventInDto;
import service.dto.event.EventOutDto;
import service.dto.event.EventShortDto;
import service.enumarated.State;
import service.model.Event;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring", uses = {CategoriesMapper.class, UserMapper.class, LocationMapper.class})
public interface EventMapper {
    EventShortDto toEventShort(Event event);

    @Mapping(target = "category", source = "category", qualifiedByName = "toEntity")
    Event toEvent(EventInDto event);

    @Mapping(target = "category", source = "category")
    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "state", source = "state", resultType = State.class)
    EventOutDto toOut(Event event);
}
