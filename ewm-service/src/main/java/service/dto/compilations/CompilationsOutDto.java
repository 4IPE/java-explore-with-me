package service.dto.compilations;

import lombok.Getter;
import lombok.Setter;
import service.dto.event.EventShortDto;

import java.util.Set;

@Getter
@Setter
public class CompilationsOutDto {
    private Set<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
