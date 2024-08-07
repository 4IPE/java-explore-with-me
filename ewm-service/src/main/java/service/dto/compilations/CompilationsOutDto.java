package service.dto.compilations;

import lombok.Getter;
import lombok.Setter;
import service.model.Event;

import java.util.Set;

@Getter
@Setter
public class CompilationsOutDto {
    private Set<Event> events;
    private Boolean pinned;
    private String title;
}
