package service.priv.service;

import ru.practicum.ewm.dto.event.EventInDto;
import ru.practicum.ewm.dto.event.EventOutDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.EventUpdDto;
import ru.practicum.ewm.dto.request.RequestOutDto;
import ru.practicum.ewm.dto.request.RequestUpdStatusDto;
import ru.practicum.ewm.dto.request.RequestUpdStatusResultDto;

import java.util.List;

public interface PrivService {
    List<EventShortDto> getEvent(Long userId, Integer from, Integer size);

    EventOutDto addEvent(EventInDto event, Long userId);

    EventOutDto getEventWithId(Long userId, Long eventId);

    EventOutDto pathEvent(Long userId, Long eventId, EventUpdDto eventUpd);

    List<RequestOutDto> getRequest(Long userId, Long eventId);

    RequestUpdStatusResultDto pathRequest(Long userId, Long eventId, RequestUpdStatusDto requestUpdStatusDto);

    List<RequestOutDto> getRequestWithOurEvent(Long userId);

    RequestOutDto addRequest(Long userId, Long eventId);

    RequestOutDto cancelRequest(Long userId, Long requestId);
}
