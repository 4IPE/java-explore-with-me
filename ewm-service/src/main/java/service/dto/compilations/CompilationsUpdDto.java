package service.dto.compilations;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Validated
public class CompilationsUpdDto {
    private List<Long> events;
    private String pinned;
    @Size(min = 1, max = 50)
    private String title;
}
