package com.bankersCalculator.server.advise.jeonseLoanAdvise.domain;

import com.bankersCalculator.server.common.enums.loanAdvise.RentalType;

public class RentalCost {
    private RentalType rentalType; // 임차타입
    private long rentalDeposit; // 임차보증금
    private long monthlyRent;   // 월세
}
