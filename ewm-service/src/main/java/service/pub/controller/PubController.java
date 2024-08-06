package service.pub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.clients.StatClient;
import ru.practicum.dto.EndpointHitInDto;
import ru.practicum.ewm.dto.categories.CategoriesOutDto;
import ru.practicum.ewm.dto.event.EventOutDto;
import service.model.Compilations;
import service.pub.service.PubService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class PubController {
    @Autowired
    private PubService pubService;
    @Autowired
    private StatClient statClient;


    @GetMapping("/categories")
    public ResponseEntity<List<CategoriesOutDto>> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                                                @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(pubService.getCategories(from, size));
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoriesOutDto> getCategoriesWithId(@PathVariable Long catId) {
        return ResponseEntity.ok().body(pubService.getCategoriesWithId(catId));
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventOutDto> getEventWithId(@PathVariable Long id, HttpServletRequest request) {

        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());

        EndpointHitInDto endpointHitInDto = new EndpointHitInDto();
        endpointHitInDto.setApp("ewm-service");
        endpointHitInDto.setIp(request.getRemoteAddr());
        endpointHitInDto.setUri(request.getRequestURI());
        endpointHitInDto.setTimestamp(LocalDateTime.now());
        statClient.addHit(endpointHitInDto);
        return ResponseEntity.ok().body(pubService.getEventWithId(id, request.getRequestURI(), statClient));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventOutDto>> getEvents(@RequestParam(name = "text", required = false) String text,
                                                       @RequestParam(name = "categories", required = false) List<Long> categories,
                                                       @RequestParam(name = "paid", required = false) Boolean paid,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                                       @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                                       @RequestParam(name = "sort", required = false) String sort,
                                                       @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                       @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                       HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        EndpointHitInDto endpointHitInDto = new EndpointHitInDto();
        endpointHitInDto.setApp("ewm-service");
        endpointHitInDto.setIp(request.getRemoteAddr());
        endpointHitInDto.setUri(request.getRequestURI());
        endpointHitInDto.setTimestamp(LocalDateTime.now());
        statClient.addHit(endpointHitInDto);
        return ResponseEntity.ok().body(pubService.getEvent(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, statClient));
    }

    @GetMapping("/compilations")
    public ResponseEntity<List<Compilations>> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                              @RequestParam(defaultValue = "0") Integer from,
                                                              @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(pubService.getCompilations(pinned, from, size));
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<Compilations> getCompilations(Long compId) {
        return ResponseEntity.ok().body(pubService.getCompilationsWithId(compId));
    }
}
