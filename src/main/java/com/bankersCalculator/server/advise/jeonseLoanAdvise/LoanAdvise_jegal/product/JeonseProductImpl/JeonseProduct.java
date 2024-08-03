package com.bankersCalculator.server.advise.jeonseLoanAdvise.LoanAdvise_jegal.product.JeonseProductImpl;

public interface JeonseProduct {

    double calculateLoanLimit(CustomerInfo customerInfo, HousingInfo housingInfo);
    double calculateInterestCost(CustomerInfo customerInfo, HousingInfo housingInfo);
    double calculateOthersCost(CustomerInfo customerInfo, HousingInfo housingInfo);
}
