package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import service.dto.compilations.CompilationsInDto;
import service.dto.compilations.CompilationsOutDto;
import service.dto.compilations.CompilationsUpdDto;
import service.dto.event.EventShortDto;
import service.exception.model.NotFoundException;
import service.model.Compilations;
import service.model.Event;
import service.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EventMapper.class)
public abstract class CompilationsMapper {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventMapper eventMapper;

    @Mapping(source = "events", target = "events", qualifiedByName = "toEvent")
    public abstract Compilations toCompilationsForIn(CompilationsInDto compilationsInDto);

    @Mapping(source = "events", target = "events", qualifiedByName = "toEvent")
    public abstract Compilations toCompilationsForUpd(CompilationsUpdDto compilationsDto);

    @Mapping(source = "events", target = "events", qualifiedByName = "toEventShortDto")
    public abstract CompilationsOutDto toCompilationsOutDto(Compilations compilations);

    @Named("toEvent")
    public Set<Event> toEntity(List<Long> eventsId) {
        if (eventsId == null || eventsId.isEmpty()) {
            return new HashSet<>();
        }
        return eventsId.stream()
                .map(id -> eventRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Event with " + id + " was not found")))
                .collect(Collectors.toSet());
    }

    @Named("toEventShortDto")
    public Set<EventShortDto> toEventShortDto(Set<Event> events) {
        if (events == null || events.isEmpty()) {
            return new HashSet<>();
        }
        return events.stream()
                .map(eventMapper::toEventShort)
                .collect(Collectors.toSet());
    }
}
