package ru.practicum.ewm.dto.compilations;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompilationsInDto {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
