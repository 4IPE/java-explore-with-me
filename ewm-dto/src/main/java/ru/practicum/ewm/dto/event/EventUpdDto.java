package ru.practicum.ewm.dto.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewm.dto.event.enumerated.StateAction;

import java.time.LocalDateTime;

@Getter
@Setter
@Validated
public class EventUpdDto {
    private String annotation;
    private Long categories;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}
