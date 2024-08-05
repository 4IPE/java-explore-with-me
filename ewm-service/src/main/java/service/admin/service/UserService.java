package service.admin.service;

import ru.practicum.ewm.dto.user.UserInDto;
import ru.practicum.ewm.dto.user.UserOutDto;

import java.util.List;

public interface UserService {
    UserOutDto addUser(UserInDto userInDto);

    List<UserOutDto> getUser(List<Long> ids, Integer from, Integer size);

    void delUser(Long id);
}
