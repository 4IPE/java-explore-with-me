package ru.practicum.ewm.dto.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewm.dto.event.enumerated.StateAction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Validated
public class EventUpdDto {
    @Size(min = 20, max = 2000)
    @NotNull
    @NotEmpty
    @NotBlank
    private String annotation;
    private Long categories;
    @Size(min = 20, max = 254)
    @NotNull
    @NotEmpty
    @NotBlank
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Size(min = 3, max = 120)
    @NotNull
    @NotEmpty
    @NotBlank
    private String title;
}
