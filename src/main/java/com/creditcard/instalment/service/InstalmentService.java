package com.creditcard.instalment.service;

import com.creditcard.instalment.model.EligibleTransactionsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service layer responsible for fetching instalment-eligible transactions
 * from a downstream API using Spring RestTemplate.
 */
@Slf4j
@Service
public class InstalmentService {

    private final RestTemplate restTemplate;
    private final String downstreamBaseUrl;
    private final String eligibleTransactionsPath;

    public InstalmentService(
            RestTemplate restTemplate,
            @Value("${downstream.api.base-url}") String downstreamBaseUrl,
            @Value("${downstream.api.eligible-transactions-path}") String eligibleTransactionsPath) {
        this.restTemplate = restTemplate;
        this.downstreamBaseUrl = downstreamBaseUrl;
        this.eligibleTransactionsPath = eligibleTransactionsPath;
    }

    /**
     * Fetches eligible instalment transactions from the downstream API.
     *
     * @return EligibleTransactionsResponse containing the list of transactions
     * @throws RestClientException if the downstream call fails
     */
    public EligibleTransactionsResponse fetchEligibleTransactions() {
        String downstreamUrl = downstreamBaseUrl + eligibleTransactionsPath;

        log.debug("Calling downstream API at: {}", downstreamUrl);

        ResponseEntity<EligibleTransactionsResponse> responseEntity =
                restTemplate.getForEntity(downstreamUrl, EligibleTransactionsResponse.class);

        log.debug("Downstream API responded with HTTP status: {}", responseEntity.getStatusCode());

        EligibleTransactionsResponse response = responseEntity.getBody();

        if (response == null || response.getEligibleTransactions() == null) {
            log.warn("Downstream API returned empty or null response body");
            return new EligibleTransactionsResponse(java.util.Collections.emptyList());
        }

        log.debug("Fetched {} eligible transaction(s) from downstream",
                response.getEligibleTransactions().size());

        return response;
    }
}
