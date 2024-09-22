package com.myZipPlan.server.calculator.repaymentCalc.dto;

import com.myZipPlan.server.calculator.repaymentCalc.domain.RepaymentSchedule;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Getter
public class RepaymentCalcResponse {
    private List<RepaymentSchedule> repaymentSchedules;
    private BigDecimal totalPrincipal; // 총 원금
    private BigDecimal totalInterest; // 총 이자
    private BigDecimal totalInstallments; // 총 상환회차

    @Builder

    public RepaymentCalcResponse(List<RepaymentSchedule> repaymentSchedules, BigDecimal totalPrincipal, BigDecimal totalInterest, BigDecimal totalInstallments) {
        this.repaymentSchedules = repaymentSchedules;
        this.totalPrincipal = truncateDecimal(totalPrincipal);
        this.totalInterest = truncateDecimal(totalInterest);
        this.totalInstallments = truncateDecimal(totalInstallments);
    }

    private BigDecimal truncateDecimal(BigDecimal value) {
        return value != null ? value.setScale(0, RoundingMode.HALF_UP) : null;
    }
}
