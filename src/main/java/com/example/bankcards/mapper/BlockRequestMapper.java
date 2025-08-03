package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.BlockRequestCreateRequest;
import com.example.bankcards.dto.response.BlockRequestResponse;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.BlockRequestStatus;
import com.example.bankcards.util.CardUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BlockRequestMapper {
    
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