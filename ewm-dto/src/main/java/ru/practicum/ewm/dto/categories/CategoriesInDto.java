package ru.practicum.ewm.dto.categories;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@Validated
public class CategoriesInDto {
    @NotBlank(message = "Имя не должно состоять из пробелов")
    @NotEmpty(message = "Имя не должно быть пустым ")
    private String name;
}
