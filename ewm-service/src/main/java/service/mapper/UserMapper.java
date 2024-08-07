package service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import service.dto.user.UserInDto;
import service.dto.user.UserOutDto;
import service.dto.user.UserShortDto;
import service.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface UserMapper {
    User toUser(UserInDto userInDto);

    UserOutDto toUserOutDto(User user);

    UserShortDto toUserShort(User user);
}
