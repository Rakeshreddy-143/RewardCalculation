package com.rewards.retail.controller;

import com.rewards.retail.dto.RewardResponse;
import com.rewards.retail.service.TransactionService;
import com.rewards.retail.validation.RewardRequestValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private RewardRequestValidator rewardRequestValidator;

    @Test
    void testGetRewardsForCustomer_ValidParams() throws Exception {
        Mockito.when(transactionService.calculateRewardsForCustomer(Mockito.eq(1L), Mockito.any(), Mockito.any()))
                .thenReturn(new RewardResponse(new HashMap<>()));

        mockMvc.perform(get("/api/v1/rewards/customer/1")
                        .param("from", "2024-01-01")
                        .param("to", "2024-03-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetRewardsForCustomer_InvalidDate() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/customer/1")
                        .param("from", "invalid-date")
                        .param("to", "2024-03-31")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetRewardsForCustomer_DifferentCustomerId() throws Exception {
        Mockito.when(transactionService.calculateRewardsForCustomer(Mockito.eq(2L), Mockito.any(), Mockito.any()))
                .thenReturn(new RewardResponse(new HashMap<>()));

        mockMvc.perform(get("/api/v1/rewards/customer/2")
                        .param("from", "2024-02-01")
                        .param("to", "2024-02-28")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}