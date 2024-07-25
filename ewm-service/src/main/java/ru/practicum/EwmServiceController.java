package ru.practicum;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.clients.StatClient;
import ru.practicum.dto.EndpointHitInDto;
import ru.practicum.dto.EndpointHitOutDto;

import java.util.List;

@RestController
@RequestMapping
@Validated
public class EwmServiceController {
    @Autowired
    private StatClient statClient;


    @PostMapping("/hit")
    public ResponseEntity<String> addHit(@RequestBody EndpointHitInDto hit) {
        return statClient.addHit(hit);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<EndpointHitOutDto>> getStats(@RequestParam(name = "start") String start,
                                                            @RequestParam(name = "end") String end,
                                                            @RequestParam(name = "uris", required = false) List<String> uris,
                                                            @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique) {
        return statClient.getStats(start, end, uris, unique);
    }
}
