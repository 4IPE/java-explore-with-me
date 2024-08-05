package ru.practicum.ewm.dto.event;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.dto.categories.CategoriesOutDto;
import ru.practicum.ewm.dto.user.UserShortDto;

@Getter
@Setter
public class EventShortDto {
    private String annotation;
    private CategoriesOutDto categories;
    private Integer confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}
