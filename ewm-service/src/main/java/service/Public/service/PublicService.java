package service.Public.service;

import ru.practicum.clients.StatClient;
import service.dto.categories.CategoriesOutDto;
import service.dto.compilations.CompilationsOutDto;
import service.dto.event.EventOutDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicService {

    List<CategoriesOutDto> getCategories(Integer from, Integer size);

    CategoriesOutDto getCategoriesWithId(Long catId);

    EventOutDto getEventWithId(Long id, String uri, StatClient statClient);

    List<EventOutDto> getEvent(String text,
                               List<Long> categories,
                               Boolean paid,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               Boolean onlyAvailable,
                               String sort,
                               Integer from,
                               Integer size,
                               StatClient statClient);

    List<CompilationsOutDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationsOutDto getCompilationsWithId(Long compId);
}
