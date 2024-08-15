package service.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class UserInDto {
    @NotEmpty(message = "Пустой email")
    @NotBlank(message = "Пустой email")
    @Email(message = "Некорректный email")
    @Size(min = 6, max = 254)
    private String email;
    @NotBlank(message = "Имя не должно состоять из пробелов")
    @NotEmpty(message = "Имя не должно быть пустым ")
    @Size(min = 2, max = 250)
    private String name;
}
