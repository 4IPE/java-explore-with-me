package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.ewm.dto.user.UserInDto;
import ru.practicum.ewm.dto.user.UserOutDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import service.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface UserMapper {
    User toUser(UserInDto userInDto);

    UserOutDto toUserOutDto(User user);

    UserShortDto toUserShort(User user);
}
