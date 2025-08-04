package com.example.bankcards.controller;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.request.UserUpdateRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.bankcards.util.ValidationValues.Page.LIMIT_DEFAULT_VALUE;
import static com.example.bankcards.util.ValidationValues.Page.OFFSET_DEFAULT_VALUE;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Управление пользователями", description = "Операции с пользователями системы (только для администраторов)")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Получить список пользователей",
            description = "Возвращает список всех пользователей системы с пагинацией",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список пользователей успешно получен",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))
                    )
            }
    )
    public List<UserResponse> getUsers(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = OFFSET_DEFAULT_VALUE) Integer page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE) Integer size
    ) {
        return userService.getAll(page, size);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает детальную информацию о пользователе по его идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о пользователе успешно получена",
                            content = @Content(schema = @Schema(implementation = UserResponse.class)))
            }
    )
    public UserResponse getUserById(
            @Parameter(description = "ID пользователя", example = "d3d94468-2d6a-4d2a-9f38-0a9d27f8c1b3")
            @PathVariable("userId") UUID userId
    ) {
        return userService.getById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Создать нового пользователя",
            description = "Регистрирует нового пользователя в системе",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Пользователь успешно создан",
                            content = @Content(schema = @Schema(implementation = UUID.class)))
            }
    )
    public UUID create(@Valid @RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить пароль пользователя",
            description = "Изменяет пароль пользователя (только для администраторов)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Пароль успешно обновлен")
            }
    )
    public void update(
            @Parameter(description = "ID пользователя", example = "d3d94468-2d6a-4d2a-9f38-0a9d27f8c1b3")
            @PathVariable("userId") UUID userId,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        userService.update(userId, request);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя из системы (только для администраторов)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален")
            }
    )
    public void delete(
            @Parameter(description = "ID пользователя", example = "d3d94468-2d6a-4d2a-9f38-0a9d27f8c1b3")
            @PathVariable("userId") UUID userId
    ) {
        userService.delete(userId);
    }
}
