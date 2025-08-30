package com.example.bankcards.service.impl;

import com.example.bankcards.dto.response.BlockRequestResponse;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.enums.BlockRequestStatusEnum;
import com.example.bankcards.exception.custom.BlockRequestNotFoundException;
import com.example.bankcards.mapper.BlockRequestMapper;
import com.example.bankcards.repository.BlockRequestRepository;
import com.example.bankcards.service.BlockRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация {@link com.example.bankcards.service.BlockRequestService}.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BlockRequestServiceImpl implements BlockRequestService {
    
    private final BlockRequestRepository blockRequestRepository;
    private final BlockRequestMapper blockRequestMapper;

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<BlockRequestResponse> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<BlockRequest> blockRequests = blockRequestRepository.findAllWithRequester(pageRequest);
        return blockRequests.getContent().stream()
                .map(blockRequestMapper::toResponse)
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<BlockRequestResponse> getByStatus(String status, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        BlockRequestStatusEnum requestStatus = BlockRequestStatusEnum.valueOf(status.toUpperCase());
        Page<BlockRequest> blockRequests = blockRequestRepository.findByStatusWithRequester(requestStatus, pageRequest);
        return blockRequests.getContent().stream()
                .map(blockRequestMapper::toResponse)
                .collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public void approve(UUID requestId) {
        BlockRequest blockRequest = requireById(requestId);
        
        blockRequest.setStatus(BlockRequestStatusEnum.APPROVED);
        blockRequest.setProcessedDate(LocalDateTime.now());
        blockRequestRepository.save(blockRequest);
    }

    /** {@inheritDoc} */
    @Override
    public void reject(UUID requestId) {
        BlockRequest blockRequest = requireById(requestId);
        
        blockRequest.setStatus(BlockRequestStatusEnum.REJECTED);
        blockRequest.setProcessedDate(LocalDateTime.now());
        blockRequestRepository.save(blockRequest);
    }

    /**
     * Проверка наличия в базе и получение запроса блокировки по id.
     * @param requestId id запроса блокировки
     * @return сущность запроса блокировки
     */
    private BlockRequest requireById(UUID requestId) {
        return blockRequestRepository.findById(requestId)
                .orElseThrow(() -> new BlockRequestNotFoundException("Block request not found with id: " + requestId));
    }
} 