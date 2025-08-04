package com.example.bankcards.service;

import com.example.bankcards.dto.response.BlockRequestResponse;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.BlockRequestStatus;
import com.example.bankcards.exception.custom.BlockRequestNotFoundException;
import com.example.bankcards.mapper.BlockRequestMapper;
import com.example.bankcards.repository.BlockRequestRepository;
import com.example.bankcards.service.impl.BlockRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockRequestServiceTest {

    @Mock
    private BlockRequestRepository blockRequestRepository;

    @Mock
    private BlockRequestMapper blockRequestMapper;

    @InjectMocks
    private BlockRequestServiceImpl blockRequestService;

    private BlockRequest blockRequest;
    private BlockRequestResponse blockRequestResponse;
    private UUID requestId;
    private User user;
    private String cardNumber;

    @BeforeEach
    void setUp() {
        requestId = UUID.randomUUID();
        cardNumber = "1234567890123456";
        user = User.builder()
                .userId(UUID.randomUUID())
                .login("testuser")
                .passwordHash("password123")
                .build();
        blockRequest = BlockRequest.builder()
                .requestId(requestId)
                .cardNumber(cardNumber)
                .requester(user)
                .status(BlockRequestStatus.PENDING)
                .requestDate(LocalDateTime.now())
                .reason("User requested card block")
                .build();
        blockRequestResponse = BlockRequestResponse.builder()
                .requestId(requestId)
                .cardNumber(cardNumber)
                .requesterLogin(user.getLogin())
                .status(BlockRequestStatus.PENDING.name())
                .requestDate(blockRequest.getRequestDate())
                .processedDate(null)
                .reason(blockRequest.getReason())
                .build();
    }

    @Test
    void getAll_ShouldReturnBlockRequestList() {
        // given
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<BlockRequest> blockRequestPage = new PageImpl<>(Collections.singletonList(blockRequest));
        when(blockRequestRepository.findAllWithRequester(pageRequest)).thenReturn(blockRequestPage);
        when(blockRequestMapper.toResponse(blockRequest)).thenReturn(blockRequestResponse);

        // when
        List<BlockRequestResponse> result = blockRequestService.getAll(page, size);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(blockRequestResponse);
        verify(blockRequestRepository).findAllWithRequester(pageRequest);
        verify(blockRequestMapper).toResponse(blockRequest);
    }

    @Test
    void getByStatus_ShouldReturnBlockRequestList_WhenStatusExists() {
        // given
        String status = "PENDING";
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<BlockRequest> blockRequestPage = new PageImpl<>(Collections.singletonList(blockRequest));
        when(blockRequestRepository.findByStatusWithRequester(BlockRequestStatus.PENDING, pageRequest)).thenReturn(blockRequestPage);
        when(blockRequestMapper.toResponse(blockRequest)).thenReturn(blockRequestResponse);

        // when
        List<BlockRequestResponse> result = blockRequestService.getByStatus(status, page, size);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(blockRequestResponse);
        verify(blockRequestRepository).findByStatusWithRequester(BlockRequestStatus.PENDING, pageRequest);
        verify(blockRequestMapper).toResponse(blockRequest);
    }

    @Test
    void getByStatus_ShouldThrowException_WhenInvalidStatus() {
        // given
        String invalidStatus = "INVALID";
        int page = 0;
        int size = 10;

        // when & then
        assertThatThrownBy(() -> blockRequestService.getByStatus(invalidStatus, page, size))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No enum constant " + BlockRequestStatus.class.getCanonicalName() + "." + invalidStatus.toUpperCase());
        verify(blockRequestRepository, never()).findByStatusWithRequester(any(), any());
        verify(blockRequestMapper, never()).toResponse(any());
    }

    @Test
    void approve_ShouldApproveBlockRequest_WhenRequestExists() {
        // given
        when(blockRequestRepository.findById(requestId)).thenReturn(Optional.of(blockRequest));
        when(blockRequestRepository.save(blockRequest)).thenReturn(blockRequest);

        // when
        blockRequestService.approve(requestId);

        // then
        assertThat(blockRequest.getStatus()).isEqualTo(BlockRequestStatus.APPROVED);
        assertThat(blockRequest.getProcessedDate()).isNotNull();
        verify(blockRequestRepository).findById(requestId);
        verify(blockRequestRepository).save(blockRequest);
    }

    @Test
    void approve_ShouldThrowException_WhenRequestDoesNotExist() {
        // given
        when(blockRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> blockRequestService.approve(requestId))
                .isInstanceOf(BlockRequestNotFoundException.class)
                .hasMessage("Block request not found with id: " + requestId);
        verify(blockRequestRepository).findById(requestId);
        verify(blockRequestRepository, never()).save(any());
    }

    @Test
    void reject_ShouldRejectBlockRequest_WhenRequestExists() {
        // given
        when(blockRequestRepository.findById(requestId)).thenReturn(Optional.of(blockRequest));
        when(blockRequestRepository.save(blockRequest)).thenReturn(blockRequest);

        // when
        blockRequestService.reject(requestId);

        // then
        assertThat(blockRequest.getStatus()).isEqualTo(BlockRequestStatus.REJECTED);
        assertThat(blockRequest.getProcessedDate()).isNotNull();
        verify(blockRequestRepository).findById(requestId);
        verify(blockRequestRepository).save(blockRequest);
    }

    @Test
    void reject_ShouldThrowException_WhenRequestDoesNotExist() {
        // given
        when(blockRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> blockRequestService.reject(requestId))
                .isInstanceOf(BlockRequestNotFoundException.class)
                .hasMessage("Block request not found with id: " + requestId);
        verify(blockRequestRepository).findById(requestId);
        verify(blockRequestRepository, never()).save(any());
    }
}