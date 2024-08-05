package ru.practicum.ewm.dto.request;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.dto.request.enumerated.StatusUpd;

import java.util.List;

@Getter
@Setter
public class RequestUpdStatusDto {
    private List<Long> requestIds;
    private StatusUpd status;
}
