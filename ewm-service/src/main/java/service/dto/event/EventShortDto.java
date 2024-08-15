package service.dto.event;

import lombok.Getter;
import lombok.Setter;
import service.dto.categories.CategoriesOutDto;
import service.dto.user.UserShortDto;

@Getter
@Setter
public class EventShortDto {
    private String annotation;
    private CategoriesOutDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}
