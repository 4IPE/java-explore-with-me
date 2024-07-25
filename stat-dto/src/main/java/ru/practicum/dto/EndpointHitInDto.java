package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndpointHitInDto {
    private String app;
    private String uri;
    private String ip;
    private String dateHit;

    @Override
    public String toString() {
        return "EndpointHitInDto{" +
                "app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", ip='" + ip + '\'' +
                ", dateHit='" + dateHit + '\'' +
                '}';
    }
}
