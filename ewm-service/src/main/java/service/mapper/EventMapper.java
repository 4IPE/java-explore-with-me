package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.ewm.dto.event.EventInDto;
import ru.practicum.ewm.dto.event.EventOutDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import service.model.Event;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring", uses = {CategoriesMapper.class, UserMapper.class})
public interface EventMapper {
    EventShortDto toEventShort(Event event);

    @Mapping(target = "categories", source = "categories", qualifiedByName = "toEntity")
    Event toEvent(EventInDto event);

    @Mapping(target = "state", ignore = true)
    EventOutDto toOut(Event event);
}
