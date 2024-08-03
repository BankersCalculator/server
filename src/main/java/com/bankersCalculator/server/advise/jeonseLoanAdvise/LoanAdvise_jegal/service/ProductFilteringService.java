package com.bankersCalculator.server.advise.jeonseLoanAdvise.LoanAdvise_jegal.service;

public class ProductFilteringService {
    public List<JeonseProduct> filterProducts(CustomerInfo customerInfo, HousingInfo housingInfo) {
        List<JeonseProduct> allProducts = JeonseProductFactory.getAllProducts();
        List<JeonseProduct> filteredProducts = new ArrayList<>();

        for (JeonseProduct product : allProducts) {

        }
        return filteredProducts;
    }
}
