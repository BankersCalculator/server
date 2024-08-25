package com.bankersCalculator.server.calculator.repaymentCalc.dto;

import com.bankersCalculator.server.calculator.repaymentCalc.domain.RepaymentSchedule;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RepaymentCalcResponse {
    private List<RepaymentSchedule> repaymentSchedules;
    private Double totalPrincipal; // 총 원금
    private Double totalInterest; // 총 이자
    private Integer totalInstallments; // 총 상환회차
}
