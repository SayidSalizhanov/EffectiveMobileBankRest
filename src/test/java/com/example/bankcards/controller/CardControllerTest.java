package com.example.bankcards.controller;

import com.example.bankcards.controller.impl.CardController;
import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.BlockRequestResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.BlockRequestService;
import com.example.bankcards.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CardService cardService;

    @MockitoBean
    private BlockRequestService blockRequestService;

    private final String CARD_NUMBER = "1234567890123456";
    private final UUID USER_ID = UUID.randomUUID();
    private final UUID REQUEST_ID = UUID.randomUUID();

    @Test
    void getCards_ShouldReturnCardList() throws Exception {
        // given
        CardResponse cardResponse = CardResponse.builder()
                .number(CARD_NUMBER)
                .expirationDate(YearMonth.now().plusYears(1))
                .status("ACTIVE")
                .balance(BigDecimal.valueOf(1000))
                .ownerId(USER_ID)
                .build();

        List<CardResponse> cards = Collections.singletonList(cardResponse);
        given(cardService.getAll(anyInt(), anyInt())).willReturn(cards);

        // when & then
        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(CARD_NUMBER))
                .andExpect(jsonPath("$[0].ownerId").value(USER_ID.toString()));
    }

    @Test
    void getCardByNumber_ShouldReturnCard() throws Exception {
        // given
        CardResponse cardResponse = CardResponse.builder()
                .number(CARD_NUMBER)
                .expirationDate(YearMonth.now().plusYears(1))
                .status("ACTIVE")
                .balance(BigDecimal.valueOf(1000))
                .ownerId(USER_ID)
                .build();

        given(cardService.getByNumber(CARD_NUMBER)).willReturn(cardResponse);

        // when & then
        mockMvc.perform(get("/api/cards/{number}", CARD_NUMBER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(CARD_NUMBER))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    void createCard_ShouldCreateCard() throws Exception {
        // given
        CardCreateRequest request = new CardCreateRequest(
                CARD_NUMBER,
                YearMonth.now().plusYears(1),
                USER_ID
        );

        willDoNothing().given(cardService).create(request);

        // when & then
        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void blockCard_ShouldBlockCard() throws Exception {
        // given
        willDoNothing().given(cardService).block(CARD_NUMBER);

        // when & then
        mockMvc.perform(post("/api/cards/{number}/block", CARD_NUMBER))
                .andExpect(status().isOk());
    }

    @Test
    void activateCard_ShouldActivateCard() throws Exception {
        // given
        willDoNothing().given(cardService).activate(CARD_NUMBER);

        // when & then
        mockMvc.perform(post("/api/cards/{number}/activate", CARD_NUMBER))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCard_ShouldDeleteCard() throws Exception {
        // given
        willDoNothing().given(cardService).delete(CARD_NUMBER);

        // when & then
        mockMvc.perform(delete("/api/cards/{number}", CARD_NUMBER))
                .andExpect(status().isNoContent());
    }

    @Test
    void getCardBalance_ShouldReturnBalance() throws Exception {
        // given
        BalanceResponse balanceResponse = new BalanceResponse(CARD_NUMBER, BigDecimal.valueOf(1500));
        given(cardService.getBalance(CARD_NUMBER)).willReturn(balanceResponse);

        // when & then
        mockMvc.perform(get("/api/cards/{number}/balance", CARD_NUMBER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value(CARD_NUMBER))
                .andExpect(jsonPath("$.balance").value(1500));
    }

    @Test
    void blockCardRequest_ShouldCreateBlockRequest() throws Exception {
        // given
        willDoNothing().given(cardService).blockRequest(CARD_NUMBER);

        // when & then
        mockMvc.perform(post("/api/cards/{number}/block-request", CARD_NUMBER))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBlockRequests_ShouldReturnRequests() throws Exception {
        // given
        BlockRequestResponse response = BlockRequestResponse.builder()
                .requestId(REQUEST_ID)
                .cardNumber(CARD_NUMBER)
                .requesterLogin("user@example.com")
                .status("PENDING")
                .requestDate(LocalDateTime.now())
                .reason("Fraud detected")
                .build();

        List<BlockRequestResponse> responses = Collections.singletonList(response);
        given(blockRequestService.getAll(anyInt(), anyInt())).willReturn(responses);

        // when & then
        mockMvc.perform(get("/api/cards/block-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value(CARD_NUMBER))
                .andExpect(jsonPath("$[0].requesterLogin").value("user@example.com"));
    }

    @Test
    void getBlockRequestsByStatus_ShouldReturnFilteredRequests() throws Exception {
        // given
        BlockRequestResponse response = BlockRequestResponse.builder()
                .requestId(REQUEST_ID)
                .cardNumber(CARD_NUMBER)
                .requesterLogin("admin@example.com")
                .status("APPROVED")
                .requestDate(LocalDateTime.now())
                .reason("Confirmed fraud")
                .build();

        List<BlockRequestResponse> responses = Collections.singletonList(response);
        given(blockRequestService.getByStatus(eq("APPROVED"), anyInt(), anyInt())).willReturn(responses);

        // when & then
        mockMvc.perform(get("/api/cards/block-requests/status/APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("APPROVED"))
                .andExpect(jsonPath("$[0].requesterLogin").value("admin@example.com"));
    }

    @Test
    void approveBlockRequest_ShouldApproveRequest() throws Exception {
        // given
        willDoNothing().given(blockRequestService).approve(REQUEST_ID);

        // when & then
        mockMvc.perform(post("/api/cards/block-requests/{requestId}/approve", REQUEST_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void rejectBlockRequest_ShouldRejectRequest() throws Exception {
        // given
        willDoNothing().given(blockRequestService).reject(REQUEST_ID);

        // when & then
        mockMvc.perform(post("/api/cards/block-requests/{requestId}/reject", REQUEST_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void createCard_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        // given
        CardCreateRequest invalidRequest = new CardCreateRequest(
                "123",
                YearMonth.now().minusMonths(1),
                null
        );

        // when & then
        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }
}