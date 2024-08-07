package service.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOutDto {
    private String email;
    private Long id;
    private String name;
}
