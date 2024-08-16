package service.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.dto.event.EventOutDto;
import service.dto.event.EventUpdDto;
import service.enumarated.State;
import service.enumarated.StateAction;
import service.exception.model.ImpossibilityOfActionException;
import service.exception.model.NotFoundException;
import service.mapper.EventMapper;
import service.mapper.LocationMapper;
import service.model.Event;
import service.repository.CategoriesRepository;
import service.repository.EventRepository;
import service.repository.LocationRepository;
import service.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoriesRepository categoriesRepository;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<EventOutDto> getEvent(List<Long> users,
                                      List<String> states,
                                      List<Long> categories,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      Integer from,
                                      Integer size) {
        List<String> statesString = states != null ? states : new ArrayList<>();
        LocalDateTime rangeStartVal = rangeStart != null ? rangeStart : LocalDateTime.now().minusYears(10);
        LocalDateTime rangeEndVal = rangeEnd != null ? rangeEnd : LocalDateTime.now().plusYears(10);
        Pageable pageable = PageRequest.of(from / size, size);
        List<EventOutDto> result = eventRepository.findAllEventWithState(rangeStartVal, rangeEndVal, users, statesString.stream().map(State::valueOf).collect(Collectors.toList()), categories, pageable)
                .stream()
                .map(eventMapper::toOut).toList();
        if (statesString.isEmpty()) {
            result = eventRepository.findAllEventWithState(rangeStartVal, rangeEndVal, users, null, categories, pageable)
                    .stream()
                    .map(eventMapper::toOut).collect(Collectors.toList());
        }
        result.forEach(event -> event.setConfirmedRequests(requestRepository.countRequest(event.getId())));
        return result;
    }

    @Override
    @Transactional
    public EventOutDto pathEvent(EventUpdDto eventUpdDto,
                                 Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with" + eventId + " was not found"));
        if (event.getPublishedOn() != null && eventUpdDto.getEventDate() != null && !event.getPublishedOn().plusHours(1).isAfter(eventUpdDto.getEventDate())) {
            throw new ImpossibilityOfActionException("дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
        }
        if (!event.getState().equals(State.PENDING) && eventUpdDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            throw new ImpossibilityOfActionException("событие можно публиковать, только если оно в состоянии ожидания публикации");
        }
        if (!event.getState().equals(State.PENDING) && eventUpdDto.getStateAction().equals(StateAction.REJECT_EVENT)) {
            throw new ImpossibilityOfActionException("событие можно отклонить, только если оно еще не опубликовано ");
        }
        event.setAnnotation(eventUpdDto.getAnnotation() != null ? eventUpdDto.getAnnotation() : event.getAnnotation());
        event.setCategory(eventUpdDto.getCategories() != null ? categoriesRepository.findById(eventUpdDto.getCategories()).orElse(event.getCategory()) : event.getCategory());
        event.setDescription(eventUpdDto.getDescription() != null ? eventUpdDto.getDescription() : event.getDescription());
        event.setEventDate(eventUpdDto.getEventDate() != null && eventUpdDto.getEventDate().isBefore(event.getEventDate()) ? eventUpdDto.getEventDate() : event.getEventDate());
        event.setLocation(eventUpdDto.getLocation() != null ? locationRepository.save(locationMapper.toLocation(eventUpdDto.getLocation())) : event.getLocation());

        event.setPaid(eventUpdDto.getPaid() != null ? eventUpdDto.getPaid() : event.getPaid());
        event.setParticipantLimit(eventUpdDto.getParticipantLimit() != null ? eventUpdDto.getParticipantLimit() : event.getParticipantLimit());
        event.setRequestModeration(eventUpdDto.getRequestModeration() != null ? eventUpdDto.getRequestModeration() : event.getRequestModeration());
        event.setState(eventUpdDto.getStateAction() != null && eventUpdDto.getStateAction().equals(StateAction.PUBLISH_EVENT) ? State.PUBLISHED : State.CANCELED);
        event.setTitle(eventUpdDto.getTitle() != null ? eventUpdDto.getTitle() : event.getTitle());

        return eventMapper.toOut(eventRepository.save(event));


    }

}
