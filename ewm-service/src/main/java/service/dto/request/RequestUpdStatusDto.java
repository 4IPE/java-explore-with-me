package service.dto.request;

import lombok.Getter;
import lombok.Setter;
import service.dto.request.enumerated.StatusUpd;

import java.util.List;

@Getter
@Setter
public class RequestUpdStatusDto {
    private List<Long> requestIds;
    private StatusUpd status;
}
