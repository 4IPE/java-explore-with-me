package service.priv.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.dto.event.EventInDto;
import service.dto.event.EventOutDto;
import service.dto.event.EventShortDto;
import service.dto.event.EventUpdDto;
import service.dto.request.RequestOutDto;
import service.dto.request.RequestUpdStatusDto;
import service.dto.request.RequestUpdStatusResultDto;
import service.priv.service.PrivService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivController {
    @Autowired
    private PrivService privService;


    @GetMapping("/events")
    public ResponseEntity<List<EventShortDto>> getEvent(@PathVariable Long userId,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(privService.getEvent(userId, from, size));
    }

    @PostMapping("/events")
    public ResponseEntity<EventOutDto> addEvents(@Valid @RequestBody EventInDto event,
                                                 @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(privService.addEvent(event, userId));
    }


    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventOutDto> getEventWithId(@PathVariable Long userId,
                                                      @PathVariable Long eventId) {
        return ResponseEntity.ok().body(privService.getEventWithId(userId, eventId));
    }


    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventOutDto> pathEvent(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @Valid @RequestBody EventUpdDto eventNew) {
        return ResponseEntity.ok().body(privService.pathEvent(userId, eventId, eventNew));
    }

    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<List<RequestOutDto>> getRequest(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        return ResponseEntity.ok().body(privService.getRequest(userId, eventId));
    }


    @PatchMapping("/events/{eventId}/requests")
    public ResponseEntity<RequestUpdStatusResultDto> pathRequest(@PathVariable Long userId,
                                                                 @PathVariable Long eventId,
                                                                 @RequestBody RequestUpdStatusDto requestUpdStatusDto) {
        return ResponseEntity.ok().body(privService.pathRequest(userId, eventId, requestUpdStatusDto));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RequestOutDto>> getRequestWithOurEvent(@PathVariable Long userId) {
        return ResponseEntity.ok().body(privService.getRequestWithOurEvent(userId));
    }

    @PostMapping("/requests")
    public ResponseEntity<RequestOutDto> addRequest(@PathVariable Long userId,
                                                    @RequestParam(name = "eventId") Long eventId,
                                                    HttpServletResponse response) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.of("GMT"));
        String formattedDate = DateTimeFormatter.RFC_1123_DATE_TIME.format(zonedDateTime);
        response.setHeader("Date", formattedDate);
        RequestOutDto result = privService.addRequest(userId, eventId, now);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<RequestOutDto> cancelRequest(@PathVariable Long userId,
                                                       @PathVariable Long requestId) {
        return ResponseEntity.ok().body(privService.cancelRequest(userId, requestId));
    }
}

