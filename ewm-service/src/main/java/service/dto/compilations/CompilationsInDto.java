package service.dto.compilations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Validated
public class CompilationsInDto {
    private List<Long> events;
    private String pinned;
    @NotBlank
    @NotEmpty
    @NotNull
    @Size(min = 1, max = 50)
    private String title;
}
