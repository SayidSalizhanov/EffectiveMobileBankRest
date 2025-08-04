package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.BlockRequestResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.BlockRequestService;
import com.example.bankcards.service.CardService;
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

import static com.example.bankcards.util.ValidationValues.Page.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Управление картами", description = "Операции с банковскими картами")
public class CardController {

    private final CardService cardService;
    private final BlockRequestService blockRequestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Получить список карт",
            description = "Возвращает список всех банковских карт с пагинацией",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список карт успешно получен",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CardResponse.class)))
                    )
            }
    )
    public List<CardResponse> getCards(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = OFFSET_DEFAULT_VALUE) Integer page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE) Integer size
    ) {
        return cardService.getAll(page, size);
    }

    @GetMapping("/{number}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Получить карту по номеру",
            description = "Возвращает детальную информацию о карте по её номеру",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о карте успешно получена",
                            content = @Content(schema = @Schema(implementation = CardResponse.class))
                    )
            }
    )
    public CardResponse getCardByNumber(
            @Parameter(description = "Номер карты", example = "1234567812345678")
            @PathVariable("number") String number
    ) {
        return cardService.getByNumber(number);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Создать новую карту",
            description = "Создает новую банковскую карту (только для администраторов)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Карта успешно создана")
            }
    )
    public void createCard(@Valid @RequestBody CardCreateRequest request) {
        cardService.create(request);
    }

    @PostMapping("/{number}/block")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Заблокировать карту",
            description = "Немедленно блокирует банковскую карту (только для администраторов)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта успешно заблокирована")
            }
    )
    public void blockCard(
            @Parameter(description = "Номер карты", example = "1234567812345678")
            @PathVariable("number") String number
    ) {
        cardService.block(number);
    }

    @PostMapping("/{number}/activate")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Активировать карту",
            description = "Активирует заблокированную банковскую карту (только для администраторов)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Карта успешно активирована")
            }
    )
    public void activateCard(
            @Parameter(description = "Номер карты", example = "1234567812345678")
            @PathVariable("number") String number
    ) {
        cardService.activate(number);
    }

    @DeleteMapping("/{number}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удалить карту",
            description = "Удаляет банковскую карту из системы (только для администраторов)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Карта успешно удалена")
            }
    )
    public void deleteCard(
            @Parameter(description = "Номер карты", example = "1234567812345678")
            @PathVariable("number") String number
    ) {
        cardService.delete(number);
    }

    @GetMapping("/{number}/balance")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Получить баланс карты",
            description = "Возвращает текущий баланс банковской карты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Баланс успешно получен",
                            content = @Content(schema = @Schema(implementation = BalanceResponse.class))
                    )
            }
    )
    public BalanceResponse getCardBalance(
            @Parameter(description = "Номер карты", example = "1234567812345678")
            @PathVariable("number") String number
    ) {
        return cardService.getBalance(number);
    }

    // block-requests

    @PostMapping("/{number}/block-request")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Создать запрос на блокировку",
            description = "Создает запрос на блокировку карты (только для пользователей)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запрос на блокировку создан")
            }
    )
    public void blockCardRequest(
            @Parameter(description = "Номер карты", example = "1234567812345678")
            @PathVariable("number") String number
    ) {
        cardService.blockRequest(number);
    }

    @GetMapping("/block-requests")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Получить все запросы на блокировку",
            description = "Возвращает список всех запросов на блокировку карт",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список запросов успешно получен",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BlockRequestResponse.class)))
                    )
            }
    )
    public List<BlockRequestResponse> getAllBlockRequests(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = OFFSET_DEFAULT_VALUE) Integer page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE) Integer size
    ) {
        return blockRequestService.getAll(page, size);
    }

    @GetMapping("/block-requests/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Получить запросы по статусу",
            description = "Возвращает список запросов на блокировку по указанному статусу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список запросов успешно получен",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BlockRequestResponse.class)))
                    )
            }
    )
    public List<BlockRequestResponse> getBlockRequestsByStatus(
            @Parameter(description = "Статус запроса", example = "PENDING")
            @PathVariable String status,

            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = OFFSET_DEFAULT_VALUE) Integer page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE) Integer size
    ) {
        return blockRequestService.getByStatus(status, page, size);
    }

    @PostMapping("/block-requests/{requestId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Одобрить запрос на блокировку",
            description = "Одобряет запрос на блокировку карты (только для администраторов)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Запрос успешно одобрен")
            }
    )
    public void approveBlockRequest(
            @Parameter(description = "ID запроса", example = "d3d94468-2d6a-4d2a-9f38-0a9d27f8c1b3")
            @PathVariable UUID requestId
    ) {
        blockRequestService.approve(requestId);
    }

    @PostMapping("/block-requests/{requestId}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Отклонить запрос на блокировку",
            description = "Отклоняет запрос на блокировку карты (только для администраторов)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Запрос успешно отклонен")
            }
    )
    public void rejectBlockRequest(
            @Parameter(description = "ID запроса", example = "d3d94468-2d6a-4d2a-9f38-0a9d27f8c1b3")
            @PathVariable UUID requestId
    ) {
        blockRequestService.reject(requestId);
    }
}
