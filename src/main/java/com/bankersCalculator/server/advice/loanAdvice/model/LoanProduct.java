package com.bankersCalculator.server.advice.loanAdvice.model;

public interface LoanProduct {


    // 개별 구현체에서 한도산출과 필터링을 구현하자.
    String getProperty();

    // TODO: filter 사유를 반환하는 걸로 수정 필요
    boolean filtering();

    double calculateLoanLimit();

}
