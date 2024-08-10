package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import service.dto.compilations.CompilationsInDto;
import service.dto.compilations.CompilationsOutDto;
import service.exception.model.NotFound;
import service.model.Compilations;
import service.model.Event;
import service.repository.EventRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EventMapper.class)
public abstract class CompilationsMapper {

    @Autowired
    private EventRepository eventRepository;

    @Mapping(source = "events", target = "events", qualifiedByName = "toEvent")
    public abstract Compilations toCompilations(CompilationsInDto compilationsInDto);

    @Mapping(source = "pinned", target = "pinned", qualifiedByName = "toString")
    public abstract CompilationsOutDto toCompilationsOutDto(Compilations compilations);

    @Named("toEvent")
    public Set<Event> toEntity(List<Long> eventsId) {
        return eventsId.stream()
                .map(id -> eventRepository.findById(id)
                        .orElseThrow(() -> new NotFound("Event with " + id + " was not found")))
                .collect(Collectors.toSet());
    }

    @Named("toString")
    public String toString(Boolean pinned) {
        return String.valueOf(pinned);
    }
}
