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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        endpointHit.setTimestamp(LocalDateTime.parse(endpointHitInDto.getTimestamp(), FORMATTER));
        statRepository.save(endpointHit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EndpointHitOutDto> getStat(String start, String end, List<String> uri, Boolean unq) {
        System.out.println(start);
        LocalDateTime startFormat = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endFormat = LocalDateTime.parse(end, FORMATTER);
        List<EndpointHit> endpointHits = uri.stream()
                .map(url -> statRepository.findByUri(url))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<EndpointHitOutDto> result = endpointHits.stream()
                .filter(hit -> hit.getTimestamp().isAfter(startFormat) && hit.getTimestamp().isBefore(endFormat))
                .collect(Collectors.groupingBy(
                        hit -> Arrays.asList(hit.getApp(), hit.getUri()),
                        Collectors.mapping(
                                EndpointHit::getIp,
                                Collectors.counting()
                        )
                ))
                .entrySet().stream()
                .map(entry -> {
                    List<String> key = entry.getKey();
                    String app = key.get(0);
                    String uriKey = key.get(1);
                    return new EndpointHitOutDto(
                            app,
                            uriKey,
                            entry.getValue()
                    );
                })
                .collect(Collectors.toList());

        if (unq) {
            result = endpointHits.stream()
                    .filter(hit -> hit.getTimestamp().isAfter(startFormat) && hit.getTimestamp().isBefore(endFormat))
                    .collect(Collectors.groupingBy(
                            hit -> Arrays.asList(hit.getApp(), hit.getUri()),
                            Collectors.mapping(
                                    EndpointHit::getIp,
                                    Collectors.toSet()
                            )
                    ))
                    .entrySet().stream()
                    .map(entry -> {
                        List<String> key = entry.getKey();
                        String app = key.get(0);
                        String uriKey = key.get(1);
                        return new EndpointHitOutDto(
                                app,
                                uriKey,
                                (long) entry.getValue().size()
                        );
                    })
                    .collect(Collectors.toList());
        }
        return result;
    }
}
