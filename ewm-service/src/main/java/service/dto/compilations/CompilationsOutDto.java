package service.dto.compilations;

import lombok.Getter;
import lombok.Setter;
import service.dto.event.EventOutDto;

import java.util.Set;

@Getter
@Setter
public class CompilationsOutDto {
    private String title;
    private Boolean pinned;
    private Set<EventOutDto> events;
}
