package service.pub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.clients.StatClient;
import ru.practicum.ewm.dto.categories.CategoriesOutDto;
import ru.practicum.ewm.dto.event.EventOutDto;
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
        rangeStart = rangeStart != null ? rangeStart : LocalDateTime.now();
        rangeEnd = rangeEnd != null ? rangeEnd : LocalDateTime.MAX;
        eventRepository.findAllEventWithStateForPub(rangeStart, rangeEnd, text, paid, categories != null ? categories : new ArrayList<>(),
                onlyAvailable, State.PUBLISHED, pageable).forEach(event -> {
            String uri = "/events/" + event.getId();
            event.setViews(Math.toIntExact(statClient.getStats(null, null, List.of(uri), false).getBody().get(0).getHits()));
            eventRepository.save(event);
        });
        if (sort.equalsIgnoreCase("event_date")) {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Order.desc("eventDate")));
        }
        if (sort.equalsIgnoreCase("views")) {
            pageable = PageRequest.of(from / size, size, Sort.by(Sort.Order.desc("views")));
        }


        return eventRepository.findAllEventWithStateForPub(rangeStart, rangeEnd, text, paid, categories != null ? categories : new ArrayList<>(),
                        onlyAvailable, State.PUBLISHED, pageable)
                .stream()
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
