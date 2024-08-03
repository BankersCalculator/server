package com.bankersCalculator.server.advise.jeonseLoanAdvise.LoanAdvise_jegal.dto;

public class LoanAdviseServiceRequest {

    public class CustomerInfo {
        private int age;
        private String maritalStatus;
        private boolean isNewlyweds;
        private int numberOfHouses;
        private int annualIncome;
        private int spouseAnnualIncome;
        private String childrenStatus;
        private boolean isNewborn;
        private boolean isSMEWorker;
        private String repaymentMethod;
    }

    public class HousingInfo {
        private int deposit;
        private int monthlyRent;
        private String housingType;
        private int area;
        private String location;
        private int requiredAmount;

    }
 }
