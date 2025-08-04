package com.example.bankcards.controller;

import com.example.bankcards.controller.impl.TransferController;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.exception.custom.CardNotFoundException;
import com.example.bankcards.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransferService transferService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void transfer_ShouldReturnOk_WhenTransferIsSuccessful() throws Exception {
        // given
        TransferRequest request = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.valueOf(100.00));
        doNothing().when(transferService).transfer(request);

        // when & then
        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(transferService).transfer(request);
    }

    @Test
    void transfer_ShouldReturnBadRequest_WhenInputIsInvalid() throws Exception {
        // given
        TransferRequest invalidRequest = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.valueOf(-100.00));

        // when & then
        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(transferService, never()).transfer(any());
    }

    @Test
    void transfer_ShouldReturnNotFound_WhenCardNotFound() throws Exception {
        // given
        TransferRequest request = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.valueOf(100.00));
        doThrow(new CardNotFoundException("Card not found")).when(transferService).transfer(request);

        // when & then
        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(transferService).transfer(request);
    }
}