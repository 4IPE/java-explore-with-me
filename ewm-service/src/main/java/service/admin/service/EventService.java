package service.admin.service;

import ru.practicum.ewm.dto.event.EventOutDto;
import ru.practicum.ewm.dto.event.EventUpdDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventOutDto> getEvent(List<Long> users,
                               List<String> states,
                               List<Long> categories,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               Integer from,
                               Integer size);

    EventOutDto pathEvent(EventUpdDto eventUpdDto,
                          Long eventId);
}
