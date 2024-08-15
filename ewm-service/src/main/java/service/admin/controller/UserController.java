package service.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.admin.service.UserService;
import service.dto.user.UserInDto;
import service.dto.user.UserOutDto;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService userService;

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
    public ResponseEntity<String> removeUser(@PathVariable Long userId) {
        userService.delUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Пользователь удален");
    }
}
