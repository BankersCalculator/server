package com.bankersCalculator.server.calculator.repaymentCalc.dto;

import com.bankersCalculator.server.calculator.repaymentCalc.domain.RepaymentSchedule;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RepaymentCalcResponse {
    private List<RepaymentSchedule> repaymentScheduleList;
    private double totalPrincipal; // 총 원금
    private double totalInterest; // 총 이자
    private int totalInstallments; // 총 상환회차
}
