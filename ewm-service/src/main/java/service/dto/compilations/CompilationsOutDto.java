package service.dto.compilations;

import lombok.Getter;
import lombok.Setter;
import service.model.Event;

import java.util.Set;

@Getter
@Setter
public class CompilationsOutDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private Set<Event> events;
}
