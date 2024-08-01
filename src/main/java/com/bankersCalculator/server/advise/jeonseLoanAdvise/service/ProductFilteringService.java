package com.bankersCalculator.server.advise.jeonseLoanAdvise.service;

import com.bankersCalculator.server.advise.jeonseLoanAdvise.domain.LoanProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductFilteringService {

    private final JeonseProductFactory jeonseProductFactory;

    public List<LoanProduct> filterProduct() {
        // 팩토리에서 filter를 위한 항목을 받아온다.
        // filter를 위한 DTO는 따로 지정하는 게 나을 듯?
        // 그


        return null;
    }
}
