package service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import service.dto.event.annotation.ValidEventDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Validated
public class EventInDto {
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long categories;
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @ValidEventDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
