package com.myZipPlan.server.calculator.repaymentCalc.dto;

import com.myZipPlan.server.calculator.repaymentCalc.domain.RepaymentSchedule;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class RepaymentCalcResponse {
    private List<RepaymentSchedule> repaymentSchedules;
    private BigDecimal totalPrincipal; // 총 원금
    private BigDecimal totalInterest; // 총 이자
    private BigDecimal totalInstallments; // 총 상환회차
}
