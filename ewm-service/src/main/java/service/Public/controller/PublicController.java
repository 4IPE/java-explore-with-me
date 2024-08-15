package service.Public.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.clients.StatClient;
import ru.practicum.dto.EndpointHitInDto;
import service.Public.service.PublicService;
import service.dto.categories.CategoriesOutDto;
import service.dto.compilations.CompilationsOutDto;
import service.dto.event.EventOutDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class PublicController {

    private final PublicService publicService;
    private final StatClient statClient;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoriesOutDto>> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                                                @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(publicService.getCategories(from, size));
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoriesOutDto> getCategoriesWithId(@PathVariable Long catId) {
        return ResponseEntity.ok().body(publicService.getCategoriesWithId(catId));
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventOutDto> getEventWithId(@PathVariable Long id, HttpServletRequest request) {


        EndpointHitInDto endpointHitInDto = new EndpointHitInDto();
        endpointHitInDto.setApp("ewm-service");
        endpointHitInDto.setIp(request.getRemoteAddr());
        endpointHitInDto.setUri(request.getRequestURI());
        endpointHitInDto.setTimestamp(LocalDateTime.now());
        statClient.addHit(endpointHitInDto);
        return ResponseEntity.ok().body(publicService.getEventWithId(id, request.getRequestURI(), statClient));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventOutDto>> getEvents(@RequestParam(name = "text", required = false) String text,
                                                       @RequestParam(name = "categories", required = false) List<Long> categories,
                                                       @RequestParam(name = "paid", required = false) Boolean paid,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       @PastOrPresent
                                                       @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       @FutureOrPresent
                                                       @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                                       @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                                       @RequestParam(name = "sort", required = false) String sort,
                                                       @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                       @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                                       HttpServletRequest request) {

        EndpointHitInDto endpointHitInDto = new EndpointHitInDto();
        endpointHitInDto.setApp("ewm-service");
        endpointHitInDto.setIp(request.getRemoteAddr());
        endpointHitInDto.setUri(request.getRequestURI());
        endpointHitInDto.setTimestamp(LocalDateTime.now());
        statClient.addHit(endpointHitInDto);
        return ResponseEntity.ok().body(publicService.getEvent(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, statClient));
    }

    @GetMapping("/compilations")
    public ResponseEntity<List<CompilationsOutDto>> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                                    @RequestParam(defaultValue = "0") Integer from,
                                                                    @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(publicService.getCompilations(pinned, from, size));
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationsOutDto> getCompilations(@PathVariable Long compId) {
        return ResponseEntity.ok().body(publicService.getCompilationsWithId(compId));
    }
}
