package service.dto.event;

import lombok.Getter;
import lombok.Setter;
import service.dto.categories.CategoriesOutDto;
import service.dto.user.UserShortDto;
import service.enumarated.State;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventOutDto {
    private String annotation;
    private CategoriesOutDto category;
    private Integer confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Integer views;
}
