package ru.practicum.ewm.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
