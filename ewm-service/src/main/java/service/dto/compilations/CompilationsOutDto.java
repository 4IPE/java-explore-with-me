package service.dto.compilations;

import lombok.Getter;
import lombok.Setter;
import service.dto.event.EventOutDto;

import java.util.Set;

@Getter
@Setter
public class CompilationsOutDto {
    private Long id;
    private String title;
    private String pinned;
    private Set<EventOutDto> events;
}
