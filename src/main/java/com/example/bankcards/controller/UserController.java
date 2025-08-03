package com.example.bankcards.controller;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.request.UserUpdateRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.bankcards.util.ValidationValues.Page.LIMIT_DEFAULT_VALUE;
import static com.example.bankcards.util.ValidationValues.Page.OFFSET_DEFAULT_VALUE;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getUsers(
            @RequestParam(defaultValue = OFFSET_DEFAULT_VALUE) Integer page,
            @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE) Integer size
    ) {
        return userService.getAll(page, size);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserById(@PathVariable("userId") UUID userId) {
        return userService.getById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID create(@RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("userId") UUID userId, @RequestBody UserUpdateRequest request) {
        userService.update(userId, request);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("userId") UUID userId) {
        userService.delete(userId);
    }
}
