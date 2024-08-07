package service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestUpdStatusResultDto {
    private List<RequestOutDto> confirmedRequests;
    private List<RequestOutDto> rejectedRequests;
}
