package com.bankersCalculator.server.advice.loanAdvice.model;

import com.bankersCalculator.server.common.enums.JeonseLoanProductType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class LoanProductFactory {

    private final Map<JeonseLoanProductType, LoanProduct> loanProducts;

    public LoanProductFactory(List<LoanProduct> loanProductList) {
        loanProducts = new EnumMap<>(JeonseLoanProductType.class);
        for (LoanProduct loanProduct : loanProductList) {
            loanProducts.put(loanProduct.getProductType(), loanProduct);
        }
    }

    public LoanProduct getLoanProduct(JeonseLoanProductType productType) {
        LoanProduct loanProduct = loanProducts.get(productType);
        if (loanProduct == null) {
            throw new RuntimeException("해당 상품이 존재하지 않습니다.");
        }
        return loanProduct;
    }
}
