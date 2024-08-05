package service.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.user.UserInDto;
import ru.practicum.ewm.dto.user.UserOutDto;
import service.exception.model.NotFound;
import service.mapper.UserMapper;
import service.model.User;
import service.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserOutDto addUser(UserInDto userInDto) {
        User user = userMapper.toUser(userInDto);
        return userMapper.toUserOutDto(userRepository.save(user));
    }

    @Override
    public List<UserOutDto> getUser(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageable).stream()
                    .map(userMapper::toUserOutDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findAllUsersWithIds(ids, pageable).stream()
                .map(userMapper::toUserOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFound("User with" + id + " was not found"));
        userRepository.deleteById(id);
    }
}
