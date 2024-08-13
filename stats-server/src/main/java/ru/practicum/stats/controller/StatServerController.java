package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitInDto;
import ru.practicum.dto.EndpointHitOutDto;
import ru.practicum.stats.service.StatService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatServerController {

    private final StatService service;

    @PostMapping("/hit")
    public ResponseEntity<String> addHit(@RequestBody EndpointHitInDto endpointHitInDto) {
        service.addHit(endpointHitInDto);
        log.info("Успешное сохранение объекта: " + endpointHitInDto.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body("Информация сохранена");

    }

    @GetMapping("/stats")
    public ResponseEntity<List<EndpointHitOutDto>> getStats(@RequestParam(name = "start") String start,
                                                            @RequestParam(name = "end") String end,
                                                            @RequestParam(name = "uris", required = false) List<String> uris,
                                                            @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        String decodeStart = URLDecoder.decode(start, StandardCharsets.UTF_8);
        String decodeEnd = URLDecoder.decode(end, StandardCharsets.UTF_8);
        log.info("Обработка запроса на стороне сервера для получения статистики с параметрами");
        return ResponseEntity.ok().body(service.getStat(decodeStart, decodeEnd, uris, unique));
    }
}
