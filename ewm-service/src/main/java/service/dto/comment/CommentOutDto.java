package service.dto.comment;

import lombok.Getter;
import lombok.Setter;
import service.dto.event.EventShortDto;
import service.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentOutDto {
    private UserShortDto commentator;
    private EventShortDto event;
    private LocalDateTime created;
    private String text;
}
