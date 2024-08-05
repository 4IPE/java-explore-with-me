package ru.practicum.ewm.dto.event;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.dto.categories.CategoriesOutDto;
import ru.practicum.ewm.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventOutDto {
    private String annotation;
    private CategoriesOutDto categories;
    private Integer confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Integer views;
}
