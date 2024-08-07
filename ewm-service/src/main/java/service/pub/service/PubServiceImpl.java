package service.pub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.clients.StatClient;
import ru.practicum.dto.EndpointHitOutDto;
import service.dto.categories.CategoriesOutDto;
import service.dto.event.EventOutDto;
import service.enumarated.State;
import service.exception.model.NotFound;
import service.mapper.CategoriesMapper;
import service.mapper.EventMapper;
import service.mapper.RequestMapper;
import service.model.Compilations;
import service.model.Event;
import service.repository.CategoriesRepository;
import service.repository.CompilationsRepository;
import service.repository.EventRepository;
import service.repository.RequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PubServiceImpl implements PubService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private RequestMapper requestMapper;
    @Autowired
    private CategoriesMapper categoriesMapper;
    @Autowired
    private CompilationsRepository compilationsRepository;


    @Override
    public List<CategoriesOutDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoriesRepository.findAll(pageable).stream().map(categoriesMapper::toOut).collect(Collectors.toList());
    }

    @Override
    public CategoriesOutDto getCategoriesWithId(Long catId) {
        return categoriesMapper.toOut(categoriesRepository.findById(catId)
                .orElseThrow(() -> new NotFound("Categories with" + catId + "not found")));
    }

    @Override
    public EventOutDto getEventWithId(Long id, String uri, StatClient statClient) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFound("Event with" + id + " was not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFound("Event with" + id + " was not found");
        }
        event.setViews(Math.toIntExact(statClient.getStats(null, null, List.of(uri), false).getBody().get(0).getHits()));
        Integer count = requestRepository.countRequest(id);
        if (event.getConfirmedRequests().equals(count)) {
            event.setConfirmedRequests(count);
        }
        return eventMapper.toOut(eventRepository.save(event));
    }

    @Override
    @Transactional
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

        Pageable pageable = PageRequest.of(from / size, size);

        LocalDateTime rangeStartVal = rangeStart != null ? rangeStart : LocalDateTime.now();
        LocalDateTime rangeEndVal = rangeEnd != null ? rangeEnd : LocalDateTime.now().plusYears(10);

        List<Event> events = eventRepository.findAllEventWithStateForPub(
                rangeStartVal, rangeEndVal, text, paid, categories != null ? categories : new ArrayList<>(),
                onlyAvailable, State.PUBLISHED, pageable).stream().collect(Collectors.toList());

        for (Event event : events) {
            String uri = "/events/" + event.getId();

            List<EndpointHitOutDto> stats = statClient.getStats(
                    LocalDateTime.now().minusYears(10).format(DateTimeFormatter.BASIC_ISO_DATE),
                    LocalDateTime.now().plusYears(10).format(DateTimeFormatter.BASIC_ISO_DATE),
                    List.of(uri), false).getBody();

            if (stats == null || stats.isEmpty()) {
                throw new RuntimeException("No stats returned for URI: " + uri);
            }

            event.setViews(Math.toIntExact(stats.get(0).getHits()));
            eventRepository.save(event);
        }

        if ("event_date".equalsIgnoreCase(sort)) {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Order.desc("eventDate")));
        } else if ("views".equalsIgnoreCase(sort)) {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Order.desc("views")));
        }

        return eventRepository.findAllEventWithStateForPub(
                        rangeStartVal, rangeEndVal, text, paid, categories != null ? categories : new ArrayList<>(),
                        onlyAvailable, State.PUBLISHED, pageable
                ).stream()
                .map(eventMapper::toOut)
                .collect(Collectors.toList());
    }


    @Override
    public List<Compilations> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned == null) {
            return compilationsRepository.findAll(pageable).stream().collect(Collectors.toList());

        }
        return compilationsRepository.findByPinned(pinned, pageable);
    }

    @Override
    public Compilations getCompilationsWithId(Long compId) {
        return compilationsRepository.findById(compId).orElseThrow(() -> new NotFound("Compilations with " + compId + "was not found"));
    }


}
