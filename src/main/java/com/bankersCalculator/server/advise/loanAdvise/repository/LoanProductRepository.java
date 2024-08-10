package com.bankersCalculator.server.advise.loanAdvise.repository;

import com.bankersCalculator.server.advise.loanAdvise.model.LoanProduct;
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
