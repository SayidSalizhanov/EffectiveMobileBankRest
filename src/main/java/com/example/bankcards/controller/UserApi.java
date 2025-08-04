package com.example.bankcards.controller;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.request.UserUpdateRequest;
import com.example.bankcards.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.bankcards.util.ValidationValues.Page.LIMIT_DEFAULT_VALUE;
import static com.example.bankcards.util.ValidationValues.Page.OFFSET_DEFAULT_VALUE;

@Tag(name = "Управление пользователями", description = "Операции с пользователями системы (только для администраторов)")
@RequestMapping("/api/users")
public interface UserApi {

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
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    List<UserResponse> getUsers(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = OFFSET_DEFAULT_VALUE) Integer page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE) Integer size
    );

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
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    UserResponse getUserById(
            @Parameter(description = "ID пользователя", example = "d3d94468-2d6a-4d2a-9f38-0a9d27f8c1b3")
            @PathVariable("userId") UUID userId
    );

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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    UUID create(@Valid @RequestBody UserCreateRequest request);

    @Operation(
            summary = "Обновить пароль пользователя",
            description = "Изменяет пароль пользователя (только для администраторов)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Пароль успешно обновлен")
            }
    )
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    void update(
            @Parameter(description = "ID пользователя", example = "d3d94468-2d6a-4d2a-9f38-0a9d27f8c1b3")
            @PathVariable("userId") UUID userId,
            @Valid @RequestBody UserUpdateRequest request
    );

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя из системы (только для администраторов)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален")
            }
    )
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    void delete(
            @Parameter(description = "ID пользователя", example = "d3d94468-2d6a-4d2a-9f38-0a9d27f8c1b3")
            @PathVariable("userId") UUID userId
    );
}
