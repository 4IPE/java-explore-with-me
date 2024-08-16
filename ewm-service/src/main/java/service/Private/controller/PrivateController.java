package service.Private.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.Private.service.PrivateService;
import service.dto.comment.CommentInDto;
import service.dto.comment.CommentOutDto;
import service.dto.event.EventInDto;
import service.dto.event.EventOutDto;
import service.dto.event.EventShortDto;
import service.dto.event.EventUpdDto;
import service.dto.request.RequestOutDto;
import service.dto.request.RequestUpdStatusDto;
import service.dto.request.RequestUpdStatusResultDto;

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
public class PrivateController {

    private final PrivateService privateService;

    @GetMapping("/events")
    public ResponseEntity<List<EventShortDto>> getEvent(@PathVariable Long userId,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(privateService.getEvent(userId, from, size));
    }

    @PostMapping("/events")
    public ResponseEntity<EventOutDto> addEvents(@Valid @RequestBody EventInDto event,
                                                 @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(privateService.addEvent(event, userId));
    }


    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventOutDto> getEventWithId(@PathVariable Long userId,
                                                      @PathVariable Long eventId) {
        return ResponseEntity.ok().body(privateService.getEventWithId(userId, eventId));
    }


    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventOutDto> pathEvent(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @Valid @RequestBody EventUpdDto eventNew) {
        return ResponseEntity.ok().body(privateService.pathEvent(userId, eventId, eventNew));
    }

    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<List<RequestOutDto>> getRequest(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        return ResponseEntity.ok().body(privateService.getRequest(userId, eventId));
    }


    @PatchMapping("/events/{eventId}/requests")
    public ResponseEntity<RequestUpdStatusResultDto> pathRequest(@PathVariable Long userId,
                                                                 @PathVariable Long eventId,
                                                                 @RequestBody RequestUpdStatusDto requestUpdStatusDto) {
        return ResponseEntity.ok().body(privateService.pathRequest(userId, eventId, requestUpdStatusDto));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RequestOutDto>> getRequestWithOurEvent(@PathVariable Long userId) {
        return ResponseEntity.ok().body(privateService.getRequestWithOurEvent(userId));
    }

    @PostMapping("/requests")
    public ResponseEntity<RequestOutDto> addRequest(@PathVariable Long userId,
                                                    @RequestParam(name = "eventId") Long eventId,
                                                    HttpServletResponse response) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.of("GMT"));
        String formattedDate = DateTimeFormatter.RFC_1123_DATE_TIME.format(zonedDateTime);
        response.setHeader("Date", formattedDate);
        RequestOutDto result = privateService.addRequest(userId, eventId, now);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<RequestOutDto> cancelRequest(@PathVariable Long userId,
                                                       @PathVariable Long requestId) {
        return ResponseEntity.ok().body(privateService.cancelRequest(userId, requestId));
    }

    @GetMapping("/event/{eventId}/comments")
    public ResponseEntity<List<CommentOutDto>> getCommentWithEventId(@PathVariable Long userId,
                                                                     @PathVariable Long eventId,
                                                                     @PositiveOrZero
                                                                     @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                                     @PositiveOrZero
                                                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(privateService.getCommentWithEventId(userId, eventId, from, size));
    }

    @GetMapping("/comment")
    public ResponseEntity<List<CommentOutDto>> getCommentWithUserId(@PathVariable Long userId,
                                                                    @PositiveOrZero
                                                                    @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                                    @PositiveOrZero
                                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(privateService.getCommentWithUserId(userId, from, size));
    }

    @GetMapping("/event/{eventId}/comment/{comId}")
    public ResponseEntity<CommentOutDto> getCommentWithId(@PathVariable Long userId,
                                                          @PathVariable Long eventId,
                                                          @PathVariable Long comId) {
        return ResponseEntity.ok().body(privateService.getCommentWithId(userId, eventId, comId));
    }


    @PostMapping("/event/{eventId}/comment")
    public ResponseEntity<CommentOutDto> addComment(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @Valid @RequestBody CommentInDto comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(privateService.addComment(userId, eventId, comment));
    }

    @PatchMapping("/event/{eventId}/comment/{comId}")
    public ResponseEntity<CommentOutDto> pathComment(@PathVariable Long userId,
                                                     @PathVariable Long eventId,
                                                     @PathVariable Long comId,
                                                     @Valid @RequestBody CommentInDto comment) {
        return ResponseEntity.ok().body(privateService.pathComment(userId, eventId, comId, comment));
    }

    @DeleteMapping("/event/{eventId}/comment/{comId}")
    public ResponseEntity<String> removeComment(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @PathVariable Long comId) {
        privateService.delComment(userId, eventId, comId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Удаление прошло");
    }
}

