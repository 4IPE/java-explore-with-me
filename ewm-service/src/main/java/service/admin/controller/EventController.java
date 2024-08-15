package service.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.admin.service.EventService;
import service.dto.event.EventOutDto;
import service.dto.event.EventUpdDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventOutDto>> getEvent(@RequestParam(name = "users", required = false) List<Long> users,
                                                      @RequestParam(name = "states", required = false) List<String> states,
                                                      @RequestParam(name = "categories", required = false) List<Long> categories,
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                      @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                      @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                                      @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                      @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        return ResponseEntity.ok().body(eventService.getEvent(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventOutDto> pathEvent(@PathVariable Long eventId,
                                                 @Valid @RequestBody EventUpdDto eventUpdDto) {
        return ResponseEntity.ok().body(eventService.pathEvent(eventUpdDto, eventId));
    }
}
