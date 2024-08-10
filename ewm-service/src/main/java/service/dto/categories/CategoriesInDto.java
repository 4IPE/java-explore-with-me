package service.dto.categories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;


@Getter
@Setter
@Validated
public class CategoriesInDto {
    @NotBlank(message = "Имя не должно состоять из пробелов")
    @NotEmpty(message = "Имя не должно быть пустым ")
    @Size(min = 1, max = 50)
    private String name;
}
