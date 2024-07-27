package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitInDto;
import ru.practicum.dto.EndpointHitOutDto;
import ru.practicum.stats.mapper.StatMapper;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private StatRepository statRepository;
    @Autowired
    private StatMapper statMapper;

    @Override
    public void addHit(EndpointHitInDto endpointHitInDto) {
        EndpointHit endpointHit = statMapper.toEndpointHit(endpointHitInDto);
        statRepository.save(endpointHit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EndpointHitOutDto> getStat(String start, String end, List<String> uri, Boolean unq) {
        LocalDateTime startFormat = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endFormat = LocalDateTime.parse(end, FORMATTER);
        List<EndpointHitOutDto> result = statRepository.findAllEndpointHitsWithoutUris(startFormat, endFormat);
        if (unq) {
            result = statRepository.findUniqueEndpointHitsWithoutUris(startFormat, endFormat);
            if (uri != null && !uri.isEmpty()) {
                result = statRepository.findUniqueEndpointHits(startFormat, endFormat, uri);
            }
        } else {
            if (uri != null && !uri.isEmpty()) {
                result = statRepository.findAllEndpointHits(startFormat, endFormat, uri);
            }
        }
        return result;
    }
}
