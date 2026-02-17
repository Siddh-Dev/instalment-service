package com.creditcard.instalment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Top-level response wrapper containing the list of instalment-eligible transactions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibleTransactionsResponse {

    @JsonProperty("eligibleTransactions")
    private List<EligibleTransaction> eligibleTransactions;
}
