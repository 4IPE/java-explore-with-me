package service.Public.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.clients.StatClient;
import ru.practicum.dto.EndpointHitOutDto;
import service.dto.categories.CategoriesOutDto;
import service.dto.compilations.CompilationsOutDto;
import service.dto.event.EventOutDto;
import service.enumarated.State;
import service.exception.model.NotFoundException;
import service.mapper.CategoriesMapper;
import service.mapper.CompilationsMapper;
import service.mapper.EventMapper;
import service.mapper.RequestMapper;
import service.model.Event;
import service.repository.CategoriesRepository;
import service.repository.CompilationsRepository;
import service.repository.EventRepository;
import service.repository.RequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {

    private static final Logger log = LoggerFactory.getLogger(PublicServiceImpl.class);
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoriesRepository categoriesRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final CategoriesMapper categoriesMapper;
    private final CompilationsRepository compilationsRepository;
    private final CompilationsMapper compilationsMapper;


    @Override
    public List<CategoriesOutDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoriesRepository.findAll(pageable).stream().map(categoriesMapper::toOut).collect(Collectors.toList());
    }

    @Override
    public CategoriesOutDto getCategoriesWithId(Long catId) {
        return categoriesMapper.toOut(categoriesRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Categories with" + catId + "not found")));
    }

    @Override
    public EventOutDto getEventWithId(Long id, String uri, StatClient statClient) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event with" + id + " was not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Event with" + id + " was not found");
        }

        List<EndpointHitOutDto> views = statClient.getStats(LocalDateTime.now().minusYears(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.now().plusYears(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), List.of(uri), true).getBody();
        event.setViews(Math.toIntExact(views != null && !views.isEmpty() ? views.getFirst().getHits() : 0));
        Integer count = requestRepository.countRequest(id);
        if (event.getConfirmedRequests().equals(count)) {
            event.setConfirmedRequests(count);
        }
        return eventMapper.toOut(event);
    }

    @Override
    public List<EventOutDto> getEvent(String text,
                                      List<Long> categories,
                                      Boolean paid,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      Boolean onlyAvailable,
                                      String sort,
                                      Integer from,
                                      Integer size,
                                      StatClient statClient) {

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        LocalDateTime rangeStartVal = rangeStart != null ? rangeStart : LocalDateTime.now().minusYears(10);
        LocalDateTime rangeEndVal = rangeEnd != null ? rangeEnd : LocalDateTime.now().plusYears(10);

        String searchText = text != null ? text.toLowerCase() : "";

        List<Event> events = eventRepository.findAllEventWithStateForPub(
                        rangeStartVal, rangeEndVal, paid, categories,
                        onlyAvailable, State.PUBLISHED, pageable).stream()
                .filter(event -> searchText.isEmpty() ||
                        (event.getAnnotation() != null && event.getAnnotation().toLowerCase().contains(searchText)) ||
                        (event.getDescription() != null && event.getDescription().toLowerCase().contains(searchText)) ||
                        (event.getTitle() != null && event.getTitle().toLowerCase().contains(searchText)))
                .toList();

        for (Event event : events) {
            String uri = "/events/" + event.getId();

            List<EndpointHitOutDto> stats = statClient.getStats(
                    LocalDateTime.now().minusYears(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.now().plusYears(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    List.of(uri), false).getBody();

            if (stats != null && !stats.isEmpty()) {
                event.setViews(Math.toIntExact(stats.get(0).getHits()));
                eventRepository.save(event);
            } else {
                event.setViews(0);
            }
        }

        if ("event_date".equalsIgnoreCase(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("eventDate")));
        } else if ("views".equalsIgnoreCase(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("views")));
        }

        return eventRepository.findAllEventWithStateForPub(
                        rangeStartVal, rangeEndVal, paid, categories,
                        onlyAvailable, State.PUBLISHED, pageable
                ).stream()
                .filter(event -> searchText.isEmpty() ||
                        (event.getAnnotation() != null && event.getAnnotation().toLowerCase().contains(searchText)) ||
                        (event.getDescription() != null && event.getDescription().toLowerCase().contains(searchText)) ||
                        (event.getTitle() != null && event.getTitle().toLowerCase().contains(searchText)))
                .map(eventMapper::toOut)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<CompilationsOutDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned == null) {
            return compilationsRepository.findAll(pageable).stream()
                    .map(compilationsMapper::toCompilationsOutDto).collect(Collectors.toList());

        }
        return compilationsRepository.findByPinned(pinned, pageable).stream()
                .map(compilationsMapper::toCompilationsOutDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationsOutDto getCompilationsWithId(Long compId) {
        return compilationsMapper.toCompilationsOutDto(compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilations with " + compId + "was not found")));
    }


}
