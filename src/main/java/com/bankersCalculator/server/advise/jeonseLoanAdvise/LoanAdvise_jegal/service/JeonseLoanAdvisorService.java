package com.bankersCalculator.server.advise.jeonseLoanAdvise.LoanAdvise_jegal.service;

import com.bankersCalculator.server.advise.jeonseLoanAdvise.LoanAdvise_jegal.product.JeonseProductImpl.JeonseProduct;

public class JeonseLoanAdvisorService {
    private final ProductFilteringService productFilteringService;
    private final LoanLimitCostEstimatorService loanLimitCostEstimatorService;
    private final ProductSelectionService productSelectionService;

    public JeonseLoanAdvisorService(ProductFilteringService productFilteringService, LoanLimitCostEstimatorService loanLimitCostEstimatorService, ProductSelectionService productSelectionService) {
        this.productFilteringService = productFilteringService;
        this.loanLimitCostEstimatorService = loanLimitCostEstimatorService;
        this.productSelectionService = productSelectionService;
    }

    public Map<String, Object> adviseLoan(CustomerInfo customerInfo, HousingInfo housingInfo) {
        List<JeonseProduct> filteredProducts = productFilteringService.filterProducts(customerInfo, housingInfo);
        Map<String, Object> costEstimates = loanLimitCostEstimatorService.estimateLoanCost(filteredProducts, new UserInput(customerInfo, housingInfo));
        return productSelectionService.selectProducts(costEstimates);
    }
    }



}
