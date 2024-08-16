package service.dto.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Valid
public class CommentInDto {
    @NotNull
    @NotBlank
    @NotEmpty
    private String text;
}
