package service.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import service.dto.event.EventOutDto;
import service.dto.event.EventUpdDto;
import service.dto.event.enumerated.StateAction;
import service.enumarated.State;
import service.exception.model.ImpossibilityOfAction;
import service.exception.model.NotFound;
import service.mapper.EventMapper;
import service.mapper.LocationMapper;
import service.model.Event;
import service.repository.CategoriesRepository;
import service.repository.EventRepository;
import service.repository.LocationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<EventOutDto> getEvent(List<Long> users,
                                      List<String> states,
                                      List<Long> categories,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      Integer from,
                                      Integer size) {
        List<Long> userId = users != null ? users : new ArrayList<>();
        List<String> statesString = states != null ? states : new ArrayList<>();
        List<Long> catId = categories != null ? categories : new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);
        List<State> stateEnum = new ArrayList<>();
        if (statesString.isEmpty()) {
            return eventRepository.findAllEventWithState(rangeStart, rangeEnd, userId, stateEnum, catId, pageable)
                    .stream()
                    .map(eventMapper::toOut).collect(Collectors.toList());
        }
        stateEnum = statesString.stream().map(State::valueOf).collect(Collectors.toList());
        return eventRepository.findAllEventWithState(rangeStart, rangeEnd, userId, stateEnum, catId, pageable)
                .stream()
                .map(eventMapper::toOut).collect(Collectors.toList());
    }

    @Override
    public EventOutDto pathEvent(EventUpdDto eventUpdDto,
                                 Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFound("Event with" + eventId + " was not found"));
        if (!event.getPublishedOn().plusHours(1).isAfter(eventUpdDto.getEventDate())) {
            throw new ImpossibilityOfAction("дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
        }
        if (!event.getState().equals(State.PENDING) && eventUpdDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            throw new ImpossibilityOfAction("событие можно публиковать, только если оно в состоянии ожидания публикации");
        }
        if (!event.getState().equals(State.PENDING) && eventUpdDto.getStateAction().equals(StateAction.REJECT_EVENT)) {
            throw new ImpossibilityOfAction("событие можно отклонить, только если оно еще не опубликовано ");
        }
        event.setAnnotation(eventUpdDto.getAnnotation() != null ? eventUpdDto.getAnnotation() : event.getAnnotation());
        event.setCategory(eventUpdDto.getCategories() != null ? categoriesRepository.findById(eventUpdDto.getCategories()).orElse(event.getCategory()) : event.getCategory());
        event.setDescription(eventUpdDto.getDescription() != null ? eventUpdDto.getDescription() : event.getDescription());
        event.setEventDate(eventUpdDto.getEventDate() != null ? eventUpdDto.getEventDate() : event.getEventDate());
        event.setLocation(eventUpdDto.getLocation() != null ? locationRepository.save(locationMapper.toEntity(eventUpdDto.getLocation())) : event.getLocation());

        event.setPaid(eventUpdDto.getPaid() != null ? eventUpdDto.getPaid() : event.getPaid());
        event.setParticipantLimit(eventUpdDto.getParticipantLimit() != null ? eventUpdDto.getParticipantLimit() : event.getParticipantLimit());
        event.setRequestModeration(eventUpdDto.getRequestModeration() != null ? eventUpdDto.getRequestModeration() : event.getRequestModeration());
        event.setState(eventUpdDto.getStateAction() != null && eventUpdDto.getStateAction().equals(StateAction.PUBLISH_EVENT) ? State.PUBLISHED : State.CANCELED);
        event.setTitle(eventUpdDto.getTitle() != null ? eventUpdDto.getTitle() : event.getTitle());

        return eventMapper.toOut(eventRepository.save(event));


    }

}
