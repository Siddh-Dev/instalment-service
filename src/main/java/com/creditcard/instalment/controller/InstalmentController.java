package com.creditcard.instalment.controller;

import com.creditcard.instalment.model.EligibleTransactionsResponse;
import com.creditcard.instalment.service.InstalmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

/**
 * Public REST controller exposing the Credit Card Instalment API.
 *
 * <p>This controller acts as the primary entry point for clients (e.g., mobile apps,
 * banking portals) to retrieve transactions eligible for instalment conversion.
 * Internally it delegates to {@link InstalmentService}, which fetches data from a
 * downstream API using Spring {@link org.springframework.web.client.RestTemplate}.</p>
 *
 * <pre>
 * Base Path: /api/v1/instalment
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/instalment")
@RequiredArgsConstructor
public class InstalmentController {

    private final InstalmentService instalmentService;

    /**
     * GET /api/v1/instalment/eligible-transactions
     *
     * <p>Returns a list of credit card transactions that are eligible for
     * conversion into an instalment plan. Data is sourced from a downstream
     * API via RestTemplate.</p>
     *
     * @return 200 OK with {@link EligibleTransactionsResponse} containing eligible transactions,
     *         or 502 Bad Gateway if the downstream call fails
     */
    @GetMapping(value = "/eligible-transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EligibleTransactionsResponse> getEligibleTransactions() {
        log.info("Received request: GET /api/v1/instalment/eligible-transactions");

        try {
            EligibleTransactionsResponse response = instalmentService.fetchEligibleTransactions();
            log.info("Successfully retrieved {} eligible transaction(s)",
                    response.getEligibleTransactions().size());
            return ResponseEntity.ok(response);

        } catch (RestClientException ex) {
            log.error("Failed to fetch data from downstream API: {}", ex.getMessage(), ex);
            return ResponseEntity.status(502).build();
        }
    }
}
