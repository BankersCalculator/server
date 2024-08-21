package com.bankersCalculator.server.advice.loanAdvice.repository;

import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LoanProductRepository {

    private final List<LoanProduct> products;

    @Autowired
    public LoanProductRepository(List<LoanProduct> products) {
        this.products = products;
    }

    public List<LoanProduct> findAll() {
        return new ArrayList<>(products);
    }

}
