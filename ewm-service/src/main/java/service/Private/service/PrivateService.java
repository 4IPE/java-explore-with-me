package service.Private.service;

import service.dto.event.EventInDto;
import service.dto.event.EventOutDto;
import service.dto.event.EventShortDto;
import service.dto.event.EventUpdDto;
import service.dto.request.RequestOutDto;
import service.dto.request.RequestUpdStatusDto;
import service.dto.request.RequestUpdStatusResultDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PrivateService {
    List<EventShortDto> getEvent(Long userId, Integer from, Integer size);

    EventOutDto addEvent(EventInDto event, Long userId);

    EventOutDto getEventWithId(Long userId, Long eventId);

    EventOutDto pathEvent(Long userId, Long eventId, EventUpdDto eventUpd);

    List<RequestOutDto> getRequest(Long userId, Long eventId);

    RequestUpdStatusResultDto pathRequest(Long userId, Long eventId, RequestUpdStatusDto requestUpdStatusDto);

    List<RequestOutDto> getRequestWithOurEvent(Long userId);

    RequestOutDto addRequest(Long userId, Long eventId, LocalDateTime createDate);

    RequestOutDto cancelRequest(Long userId, Long requestId);
}
