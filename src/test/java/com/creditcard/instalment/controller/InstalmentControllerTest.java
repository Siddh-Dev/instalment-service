package com.creditcard.instalment.controller;

import com.creditcard.instalment.model.EligibleTransaction;
import com.creditcard.instalment.model.EligibleTransactionsResponse;
import com.creditcard.instalment.service.InstalmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InstalmentController.class)
class InstalmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstalmentService instalmentService;

    @Test
    @DisplayName("GET /api/v1/instalment/eligible-transactions - returns 200 with transaction list")
    void shouldReturn200WithEligibleTransactions() throws Exception {
        // Given
        EligibleTransactionsResponse mockResponse = EligibleTransactionsResponse.builder()
                .eligibleTransactions(List.of(
                        EligibleTransaction.builder()
                                .merchantName("Amazon India")
                                .transactionDate("2026-02-10")
                                .amount(24999.00)
                                .build(),
                        EligibleTransaction.builder()
                                .merchantName("Apple Store Online")
                                .transactionDate("2026-01-30")
                                .amount(129900.00)
                                .build()
                ))
                .build();

        when(instalmentService.fetchEligibleTransactions()).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/instalment/eligible-transactions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.eligibleTransactions").isArray())
                .andExpect(jsonPath("$.eligibleTransactions.length()").value(2))
                .andExpect(jsonPath("$.eligibleTransactions[0].merchantName").value("Amazon India"))
                .andExpect(jsonPath("$.eligibleTransactions[0].transactionDate").value("2026-02-10"))
                .andExpect(jsonPath("$.eligibleTransactions[0].amount").value(24999.00))
                .andExpect(jsonPath("$.eligibleTransactions[1].merchantName").value("Apple Store Online"))
                .andExpect(jsonPath("$.eligibleTransactions[1].amount").value(129900.00));
    }

    @Test
    @DisplayName("GET /api/v1/instalment/eligible-transactions - returns 502 on downstream failure")
    void shouldReturn502WhenDownstreamFails() throws Exception {
        // Given
        when(instalmentService.fetchEligibleTransactions())
                .thenThrow(new ResourceAccessException("Downstream service unavailable"));

        // When & Then
        mockMvc.perform(get("/api/v1/instalment/eligible-transactions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadGateway());
    }
}
