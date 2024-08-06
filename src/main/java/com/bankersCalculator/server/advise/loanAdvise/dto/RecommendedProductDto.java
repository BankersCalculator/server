package com.bankersCalculator.server.advise.loanAdvise.dto;

import com.bankersCalculator.server.advise.loanAdvise.domain.LoanAdviseResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class RecommendedProductDto {
    private int rank;
    private String loanProductName;
    private String loanProductCode;
    private double possibleLoanLimit;
    private double expectedLoanRate;
    private String notEligibleReason;
}