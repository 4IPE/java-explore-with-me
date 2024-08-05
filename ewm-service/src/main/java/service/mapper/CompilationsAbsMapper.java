package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import service.exception.model.NotFound;
import service.model.Event;
import service.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CompilationsAbsMapper {
    @Autowired
    private EventRepository eventRepository;

    @Named("toEvent")
    public List<Event> toEntity(List<Long> eventsId) {
        //        Compilations compilations = new Compilations();
//        compilations.setEvents(events);
//        compilations.setPinned(compilationsInDto.getPinned()!=null?compilationsInDto.getPinned():false);
//        compilations.setTitle(compilationsInDto.getTitle());
        return eventsId.stream()
                .map(id -> eventRepository.findById(id).orElseThrow(() -> new NotFound("Event with" + id + " was not found")))
                .collect(Collectors.toList());
    }
}
