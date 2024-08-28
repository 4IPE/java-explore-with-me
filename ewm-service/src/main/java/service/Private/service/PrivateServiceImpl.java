package service.Private.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.dto.comment.CommentInDto;
import service.dto.comment.CommentOutDto;
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
import service.exception.model.ImpossibilityOfActionException;
import service.exception.model.NotFoundException;
import service.mapper.CommentMapper;
import service.mapper.EventMapper;
import service.mapper.LocationMapper;
import service.mapper.RequestMapper;
import service.model.Comment;
import service.model.Event;
import service.model.Request;
import service.model.User;
import service.repository.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateServiceImpl implements PrivateService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoriesRepository categoriesRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;


    @Override
    public List<EventShortDto> getEvent(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with" + userId + " was not found"));
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.findByInitiatorId(userId, pageable).stream()
                .map(eventMapper::toEventShort)
                .collect(Collectors.toList());
    }

    @Override
    public EventOutDto addEvent(EventInDto eventIn, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with" + userId + " was not found"));
        Event event = eventMapper.toEvent(eventIn);
        locationRepository.save(event.getLocation());
        event.setInitiator(user);
        event.setState(State.PENDING);
        return eventMapper.toOut(eventRepository.save(event));
    }

    @Override
    public EventOutDto getEventWithId(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with" + userId + " was not found"));
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with" + eventId + " was not found"));
        return eventMapper.toOut(event);
    }


    @Override
    @Transactional
    public EventOutDto pathEvent(Long userId, Long eventId, EventUpdDto eventUpd) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with" + userId + " was not found"));
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with" + eventId + " was not found"));

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ImpossibilityOfActionException("You cannot perform this action since this event is " + event.getState());
        }

        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new ImpossibilityOfActionException("дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
        event.setAnnotation(eventUpd.getAnnotation() != null ? eventUpd.getAnnotation() : event.getAnnotation());
        event.setCategory(eventUpd.getCategories() != null ? categoriesRepository.findById(eventUpd.getCategories()).orElse(event.getCategory()) : event.getCategory());
        event.setDescription(eventUpd.getDescription() != null && !eventUpd.getDescription().isEmpty() ? eventUpd.getDescription() : event.getDescription());
        event.setEventDate(eventUpd.getEventDate() != null ? eventUpd.getEventDate() : event.getEventDate());
        event.setPaid(eventUpd.getPaid() != null ? eventUpd.getPaid() : event.getPaid());
        event.setParticipantLimit(eventUpd.getParticipantLimit() != null ? eventUpd.getParticipantLimit() : event.getParticipantLimit());
        event.setRequestModeration(eventUpd.getRequestModeration() != null ? eventUpd.getRequestModeration() : event.getRequestModeration());
        event.setTitle(eventUpd.getTitle() != null ? eventUpd.getTitle() : event.getTitle());
        event.setLocation(eventUpd.getLocation() != null ? locationRepository.save(locationMapper.toLocation(eventUpd.getLocation())) : event.getLocation());
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
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with" + userId + " was not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with" + eventId + " was not found"));
        return requestRepository.findByEventId(eventId).stream()
                .map(requestMapper::toRequestOut)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestUpdStatusResultDto pathRequest(Long userId, Long eventId, RequestUpdStatusDto requestUpdStatusDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " was not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id " + eventId + " was not found"));

        if (event.getParticipantLimit() == 0 && !event.getRequestModeration()) {
            throw new ImpossibilityOfActionException("Для данного ивента не требуется подтверждения завяок ");
        }

        List<Request> requests = requestRepository.findAllById(requestUpdStatusDto.getRequestIds());
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        for (Request request : requests) {
            if (request.getStatus() != StatusUpd.PENDING) {
                throw new ImpossibilityOfActionException("Запрос находится не в ожидании ");
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
                    throw new ImpossibilityOfActionException("Закончились места");
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
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with" + userId + " was not found"));
        return requestRepository.findByRequesterId(userId).stream()
                .map(requestMapper::toRequestOut)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestOutDto addRequest(Long userId, Long eventId, LocalDateTime createDate) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with " + userId + " was not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ImpossibilityOfActionException("Event with " + eventId + " was not found"));
        Request requestFind = requestRepository.findByRequesterIdAndEventId(userId, eventId).orElse(null);
        if (requestFind != null) {
            throw new ImpossibilityOfActionException("Нельзя добавлять потворный запрос");
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ImpossibilityOfActionException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ImpossibilityOfActionException("Нельзя участвовать в неопубликованном событии ");
        }
        if (!event.getParticipantLimit().equals(0) && event.getParticipantLimit().equals(requestRepository.countRequest(event.getId()))) {
            throw new ImpossibilityOfActionException("у события достигнут лимит запросов на участие");
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
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with" + userId + " was not found"));
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request with" + requestId + " was not found"));
        request.setStatus(StatusUpd.CANCELED);
        return requestMapper.toRequestOut(request);
    }

    @Override
    public CommentOutDto addComment(Long userId,
                                    Long eventId,
                                    CommentInDto commentInDto) {
        User commentator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User don t found id: " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event don t found id: " + eventId));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ImpossibilityOfActionException("Не можете оставлять коментарии на неопубликованный ивент");
        }
        Request request = requestRepository.findByRequesterIdAndEventId(userId, eventId).orElseThrow(() -> new NotFoundException("Вы не можете оставлять комментарии , где ваша заявка не была оставлена"));
        if (!request.getStatus().equals(StatusUpd.CONFIRMED)) {
            throw new ImpossibilityOfActionException("Не можете оставлять коментарии если ваша заявка не одобрена ");
        }
        Comment comment = commentMapper.toComment(commentInDto);
        comment.setCommentator(commentator);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return commentMapper.toCommentOut(commentRepository.save(comment));
    }

    @Override
    public CommentOutDto pathComment(Long userId,
                                     Long eventId,
                                     Long commentId,
                                     CommentInDto commentInDto) {
        User activeUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User don t found id: " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event don t found id: " + eventId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Event don t found id: " + commentId));

        if (!comment.getEvent().getId().equals(event.getId()) || !comment.getCommentator().getId().equals(activeUser.getId())) {
            throw new ImpossibilityOfActionException("Ты не можешь редактировать данный комментарий");
        }

        comment.setText(commentInDto.getText());
        return commentMapper.toCommentOut(commentRepository.save(comment));
    }

    @Override
    public void delComment(Long userId,
                           Long eventId,
                           Long commentId) {
        User activeUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User don t found id: " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event don t found id: " + eventId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment don t found id: " + commentId));

        if (!comment.getEvent().getId().equals(event.getId()) || !comment.getCommentator().getId().equals(activeUser.getId())) {
            throw new ImpossibilityOfActionException("Ты не можешь удалять данный комментарий");

        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentOutDto> getCommentWithEventId(Long userId,
                                                     Long eventId,
                                                     Integer from,
                                                     Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        User activeUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User don t found id: " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event don t found id: " + eventId));
        return commentRepository.findByEventId(eventId, pageable).stream().map(commentMapper::toCommentOut).toList();
    }

    @Override
    public List<CommentOutDto> getCommentWithUserId(Long userId,
                                                    Integer from,
                                                    Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        User commentator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User don t found id: " + userId));
        return commentRepository.findByCommentatorId(userId, pageable).stream().map(commentMapper::toCommentOut).toList();
    }

    @Override
    public CommentOutDto getCommentWithId(Long userId,
                                          Long eventId,
                                          Long comId) {
        User commentator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User don t found id: " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event don t found id: " + eventId));
        return commentMapper.toCommentOut(commentRepository.findByIdAndCommentatorIdAndEventId(comId, userId, eventId).orElseThrow(() -> new NotFoundException("Comment with id: " + comId + "don t found")));
    }
}
