package ru.practicum.ewm.dto.event;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Location {
    private Long id;
    private Double lat;
    private Double lon;
}
