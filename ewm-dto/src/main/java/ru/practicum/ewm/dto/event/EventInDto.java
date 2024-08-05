package ru.practicum.ewm.dto.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewm.dto.event.annotation.ValidEventDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Validated
public class EventInDto {
    @NotNull
    private String annotation;
    @NotNull
    private Long categories;
    @NotNull
    private String description;
    @ValidEventDate
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    @NotNull
    private Integer participantLimit;
    @NotNull
    private Boolean requestModeration;
    @NotNull
    private String title;
}
