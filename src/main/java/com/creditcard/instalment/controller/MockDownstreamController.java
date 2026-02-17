package com.creditcard.instalment.controller;

import com.creditcard.instalment.model.EligibleTransaction;
import com.creditcard.instalment.model.EligibleTransactionsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Mocked downstream API controller that simulates an external service
 * returning credit card instalment-eligible transactions.
 *
 * <p>In a real-world scenario, this would be a separate microservice or
 * third-party API. Here it runs within the same application to
 * demonstrate the RestTemplate integration pattern.</p>
 *
 * Base Path: /mock/downstream
 */
@Slf4j
@RestController
@RequestMapping("/mock/downstream")
public class MockDownstreamController {

    /**
     * Simulated downstream endpoint returning a static list of
     * credit card transactions eligible for instalment conversion.
     *
     * @return 200 OK with EligibleTransactionsResponse payload
     */
    @GetMapping(value = "/eligible-transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EligibleTransactionsResponse> getEligibleTransactions() {
        log.debug("[MOCK DOWNSTREAM] Received request for eligible instalment transactions");

        EligibleTransactionsResponse response = EligibleTransactionsResponse.builder()
                .eligibleTransactions(List.of(
                        EligibleTransaction.builder()
                                .merchantName("Amazon India")
                                .transactionDate("2026-02-10")
                                .amount(24999.00)
                                .build(),
                        EligibleTransaction.builder()
                                .merchantName("Reliance Digital")
                                .transactionDate("2026-02-08")
                                .amount(58990.00)
                                .build(),
                        EligibleTransaction.builder()
                                .merchantName("Flipkart")
                                .transactionDate("2026-02-05")
                                .amount(18999.00)
                                .build(),
                        EligibleTransaction.builder()
                                .merchantName("Croma")
                                .transactionDate("2026-02-02")
                                .amount(74999.00)
                                .build(),
                        EligibleTransaction.builder()
                                .merchantName("Apple Store Online")
                                .transactionDate("2026-01-30")
                                .amount(129900.00)
                                .build()
                ))
                .build();

        log.debug("[MOCK DOWNSTREAM] Returning {} transactions", response.getEligibleTransactions().size());

        return ResponseEntity.ok(response);
    }
}
