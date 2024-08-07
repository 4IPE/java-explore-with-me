package ru.practicum.clients;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHitInDto;
import ru.practicum.dto.EndpointHitOutDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StatClient {
    private final RestTemplate restTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Value("${stat.client.url}")
    private String baseUrl;

    public StatClient() {
        this.restTemplate = new RestTemplate();
    }


    public ResponseEntity<String> addHit(EndpointHitInDto endpointHitInDto) {
        String url = baseUrl + "/hit";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EndpointHitInDto> request = new HttpEntity<>(endpointHitInDto, headers);
        return restTemplate.postForEntity(url, request, String.class);
    }

    public ResponseEntity<List<EndpointHitOutDto>> getStats(String start, String end, List<String> uris, Boolean unique) {
        String encodedStart = URLEncoder.encode(start, StandardCharsets.UTF_8);
        String encodedEnd = URLEncoder.encode(end, StandardCharsets.UTF_8);

        String url = String.format("%s/stats?start=%s&end=%s&uris=%s&unique=%s",
                baseUrl,
                encodedStart,
                encodedEnd,
                uris != null ? String.join(",", uris) : "",
                unique != null ? unique.toString() : "false");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<EndpointHitOutDto>>() {
                });
    }
}

