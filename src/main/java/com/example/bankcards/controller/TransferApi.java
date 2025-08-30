package com.example.bankcards.controller;

import com.example.bankcards.dto.request.TransferRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * REST API для переводов средств между банковскими картами.
 */
@Tag(name = "Переводы", description = "Операции перевода средств между картами")
@RequestMapping("/api/transfers")
public interface TransferApi {

    @Operation(
            summary = "Выполнить перевод",
            description = "Переводит средства между банковскими картами",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Перевод успешно выполнен")
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    /**
     * Выполняет перевод средств между банковскими картами.
     * Карты должны принадлежать одному пользователю и быть активными.
     *
     * @param request данные для перевода (карта-отправитель, карта-получатель, сумма)
     */
    void transfer(@Valid @RequestBody TransferRequest request);
}
