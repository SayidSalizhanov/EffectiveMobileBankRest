package com.example.bankcards.mapper;

import com.example.bankcards.dto.response.BlockRequestResponse;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.util.CardUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Маппер для преобразования запросов на блокировку в DTO.
 */
@Component
public class BlockRequestMapper {
    
    /**
     * Преобразует сущность BlockRequest в DTO ответа, маскируя номер карты.
     * @param blockRequest запрос на блокировку
     * @return dto запроса на блокировку (response)
     */
    public BlockRequestResponse toResponse(BlockRequest blockRequest) {
        return BlockRequestResponse.builder()
                .requestId(blockRequest.getRequestId())
                .cardNumber(CardUtil.maskCardNumber(blockRequest.getCardNumber()))
                .requesterLogin(blockRequest.getRequester().getLogin())
                .status(blockRequest.getStatus().name())
                .requestDate(blockRequest.getRequestDate())
                .processedDate(blockRequest.getProcessedDate())
                .reason(blockRequest.getReason())
                .build();
    }
} 