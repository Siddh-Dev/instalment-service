package com.creditcard.instalment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single credit card transaction eligible for instalment conversion.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibleTransaction {

    @JsonProperty("merchantName")
    private String merchantName;

    @JsonProperty("transactionDate")
    private String transactionDate;

    @JsonProperty("amount")
    private Double amount;
}
