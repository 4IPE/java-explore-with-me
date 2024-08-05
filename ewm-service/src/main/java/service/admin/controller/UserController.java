package service.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.user.UserInDto;
import ru.practicum.ewm.dto.user.UserOutDto;
import service.admin.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserOutDto> addUser(@RequestBody @Valid UserInDto user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @GetMapping
    public ResponseEntity<List<UserOutDto>> getUsers(@RequestParam(required = false) List<Long> ids,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(userService.getUser(ids, from, size));
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<String> addUser(@PathVariable Long userId) {
        userService.delUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Пользователь удален");
    }
}
