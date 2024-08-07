package service.dto.compilations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Validated
public class CompilationsInDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank
    @NotEmpty
    @NotNull
    private String title;
}
