package service.priv.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.dto.event.EventInDto;
import service.dto.event.EventOutDto;
import service.dto.event.EventShortDto;
import service.dto.event.EventUpdDto;
import service.dto.request.RequestOutDto;
import service.dto.request.RequestUpdStatusDto;
import service.dto.request.RequestUpdStatusResultDto;
import service.enumarated.State;
import service.enumarated.StateAction;
import service.enumarated.StatusUpd;
import service.exception.model.ImpossibilityOfAction;
import service.exception.model.NotFound;
import service.mapper.EventMapper;
import service.mapper.LocationMapper;
import service.mapper.RequestMapper;
import service.model.Event;
import service.model.Request;
import service.model.User;
import service.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrivServiceImpl implements PrivService {
    private static final Logger log = LoggerFactory.getLogger(PrivServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
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
    private LocationMapper locationMapper;
    @Autowired
    private LocationRepository locationRepository;


    @Override
    public List<EventShortDto> getEvent(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound("User with" + userId + " was not found"));
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.findByInitiatorId(userId, pageable).stream()
                .map(eventMapper::toEventShort)
                .collect(Collectors.toList());
    }

    @Override
    public EventOutDto addEvent(EventInDto eventIn, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound("User with" + userId + " was not found"));
        Event event = eventMapper.toEvent(eventIn);
        locationRepository.save(event.getLocation());
        event.setInitiator(user);
        event.setState(State.PENDING);
        EventOutDto eventOutDto = eventMapper.toOut(eventRepository.save(event));
        eventOutDto.setState(event.getState());
        return eventOutDto;
    }

    @Override
    public EventOutDto getEventWithId(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound("User with" + userId + " was not found"));
        Event event = Optional.ofNullable(eventRepository.findByIdAndInitiatorId(eventId, userId))
                .orElseThrow(() -> new NotFound("Event with" + eventId + " was not found"));
        return eventMapper.toOut(event);
    }


    @Override
    @Transactional
    public EventOutDto pathEvent(Long userId, Long eventId, EventUpdDto eventUpd) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound("User with" + userId + " was not found"));
        Event event = Optional.ofNullable(eventRepository.findByIdAndInitiatorId(eventId, userId))
                .orElseThrow(() -> new NotFound("Event with" + eventId + " was not found"));
        if (eventUpd.getStateAction() != null && !event.getState().equals(State.CANCELED) && !eventUpd.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
            throw new ImpossibilityOfAction("You cannot perform this action since this event is " + event.getState());
        }
//        if (!event.getState().equals(State.PENDING)) {
//            throw new ImpossibilityOfAction("You cannot perform this action since this event is " + event.getState());
//        }

        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new ImpossibilityOfAction("дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
        event.setAnnotation(eventUpd.getAnnotation() != null ? eventUpd.getAnnotation() : event.getAnnotation());
        event.setCategory(eventUpd.getCategories() != null ? categoriesRepository.findById(eventUpd.getCategories()).orElse(event.getCategory()) : event.getCategory());
        event.setDescription(eventUpd.getDescription() != null && !eventUpd.getDescription().isEmpty() ? eventUpd.getDescription() : event.getDescription());
        event.setEventDate(eventUpd.getEventDate() != null ? eventUpd.getEventDate() : event.getEventDate());
        event.setPaid(eventUpd.getPaid() != null ? eventUpd.getPaid() : event.getPaid());
        event.setParticipantLimit(eventUpd.getParticipantLimit() != null ? eventUpd.getParticipantLimit() : event.getParticipantLimit());
        event.setRequestModeration(eventUpd.getRequestModeration() != null ? eventUpd.getRequestModeration() : event.getRequestModeration());
        event.setTitle(eventUpd.getTitle() != null ? eventUpd.getTitle() : event.getTitle());
        event.setLocation(eventUpd.getLocation() != null ? locationRepository.save(locationMapper.toEntity(eventUpd.getLocation())) : event.getLocation());
        if (eventUpd.getStateAction() != null && eventUpd.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
            event.setState(State.PENDING);
        }
        if (eventUpd.getStateAction() != null && eventUpd.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
            event.setState(State.CANCELED);
        }
        return eventMapper.toOut(eventRepository.save(event));
    }

    @Override
    public List<RequestOutDto> getRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound("User with" + userId + " was not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFound("Event with" + eventId + " was not found"));
        return requestRepository.findByEventId(eventId).stream()
                .map(requestMapper::toRequestOut)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestUpdStatusResultDto pathRequest(Long userId, Long eventId, RequestUpdStatusDto requestUpdStatusDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound("User with id " + userId + " was not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFound("Event with id " + eventId + " was not found"));

        if (event.getParticipantLimit() == 0 || event.getRequestModeration()) {
            throw new ImpossibilityOfAction("Для данного ивента не требуется подтверждения завяок ");
        }

        List<Request> requests = requestRepository.findAllById(requestUpdStatusDto.getRequestIds());
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        for (Request request : requests) {
            if (request.getStatus() != StatusUpd.PENDING) {
                throw new ImpossibilityOfAction("Запрос находится не в ожидании ");
            }

            if (requestUpdStatusDto.getStatus() == StatusUpd.CONFIRMED) {
                if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() < event.getParticipantLimit()) {
                    request.setStatus(StatusUpd.CONFIRMED);
                    confirmedRequests.add(request);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);

                    if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                        List<Request> pendingRequests = requestRepository.findAllByEventIdAndStatus(eventId, StatusUpd.PENDING);
                        for (Request pendingRequest : pendingRequests) {
                            pendingRequest.setStatus(StatusUpd.REJECTED);
                            rejectedRequests.add(pendingRequest);
                        }
                        requestRepository.saveAll(pendingRequests);
                    }
                } else {
                    throw new ImpossibilityOfAction("Закончились места");
                }
            } else if (requestUpdStatusDto.getStatus() == StatusUpd.REJECTED) {
                request.setStatus(StatusUpd.REJECTED);
                rejectedRequests.add(request);
            }
        }

        requestRepository.saveAll(confirmedRequests);
        requestRepository.saveAll(rejectedRequests);
        eventRepository.save(event);

        RequestUpdStatusResultDto result = new RequestUpdStatusResultDto();
        result.setConfirmedRequests(confirmedRequests.stream().map(requestMapper::toRequestOut).collect(Collectors.toList()));
        result.setRejectedRequests(rejectedRequests.stream().map(requestMapper::toRequestOut).collect(Collectors.toList()));

        return result;
    }


    @Override
    public List<RequestOutDto> getRequestWithOurEvent(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound("User with" + userId + " was not found"));
        return requestRepository.findByRequesterId(userId).stream()
                .map(requestMapper::toRequestOut)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestOutDto addRequest(Long userId, Long eventId, LocalDateTime createDate) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound("User with" + userId + " was not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFound("Event with" + eventId + " was not found"));
        Request requestFind = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (requestFind != null) {
            throw new ImpossibilityOfAction("Нельзя добавлять потворный запрос");
        }
        if (user.getId().equals(event.getInitiator().getId())) {
            throw new ImpossibilityOfAction("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ImpossibilityOfAction("Нельзя участвовать в неопубликованном событии ");
        }
        if (!event.getParticipantLimit().equals(0) && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ImpossibilityOfAction("у события достигнут лимит запросов на участие");
        }
        Request requestCreate = new Request();
        requestCreate.setRequester(user);
        requestCreate.setEvent(event);
        requestCreate.setCreated(createDate);
        requestCreate.setStatus(StatusUpd.PENDING);
        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0)) {
            requestCreate.setStatus(StatusUpd.CONFIRMED);
            event.setConfirmedRequests(requestRepository.countRequest(event.getId()));
            eventRepository.save(event);
        }
        return requestMapper.toRequestOut(requestRepository.save(requestCreate));
    }

    @Override
    public RequestOutDto cancelRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFound("User with" + userId + " was not found"));
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFound("Request with" + requestId + " was not found"));
        request.setStatus(StatusUpd.CANCELED);
        return requestMapper.toRequestOut(request);
    }
}
