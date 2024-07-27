package ru.practicum.stats.service;

import ru.practicum.dto.EndpointHitInDto;
import ru.practicum.dto.EndpointHitOutDto;

import java.util.List;

public interface StatService {
    void addHit(EndpointHitInDto endpointHitInDto);


    List<EndpointHitOutDto> getStat(String start, String end, List<String> uri, Boolean unq);
}
