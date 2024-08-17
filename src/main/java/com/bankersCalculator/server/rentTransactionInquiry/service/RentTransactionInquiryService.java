package com.bankersCalculator.server.rentTransactionInquiry.service;

import com.bankersCalculator.server.rentTransactionInquiry.api.RentTransactionApiClient;
import com.bankersCalculator.server.rentTransactionInquiry.common.RentHousingType;
import com.bankersCalculator.server.rentTransactionInquiry.dto.RentTransactionApiResponse;
import com.bankersCalculator.server.rentTransactionInquiry.dto.RentTransactionInquiryResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RentTransactionInquiryService {

    private final RentTransactionApiClient apiClient;

    public RentTransactionInquiryService(RentTransactionApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public RentTransactionInquiryResponse getRentTransactionsResult(String lawdCd, RentHousingType rentHousingType, int months, String emdNm, String jibun) throws IOException {
        // 현재 날짜를 기준으로 조회할 월 리스트 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        LocalDate currentDate = LocalDate.now();

        List<RentTransactionInquiryResponse.TransactionDetail> transactions = currentDate.minusMonths(months - 1)
                .datesUntil(currentDate.plusMonths(1), java.time.Period.ofMonths(1))
                .map(date -> date.format(formatter))
                .flatMap(dealYmd -> {
                    try {
                        RentTransactionApiResponse apiResponse = apiClient.RentTransactionCallApi(lawdCd, dealYmd, rentHousingType);
                        return apiResponse.getBody().getItems().getItemList().stream()
                                .filter(item -> item.getUmdNm().equals(emdNm) && item.getJibun().equals(jibun))
                                .map(item -> new RentTransactionInquiryResponse.TransactionDetail(
                                        item.getAptNm(),
                                        item.getDealYear(),
                                        item.getDealMonth(),
                                        item.getDeposit(),
                                        item.getExcluUseAr(),
                                        item.getFloor(),
                                        item.getMonthlyRent(),
                                        item.getContractType()
                                ));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        // 평형별 보증금, 월세 평균 및 건수 계산
        Map<String, RentTransactionInquiryResponse.AverageInfo> averageInfoMap = calculateAverageByExcluUseAr(transactions);

        return new RentTransactionInquiryResponse(averageInfoMap, transactions);
    }

    private Map<String, RentTransactionInquiryResponse.AverageInfo> calculateAverageByExcluUseAr(List<RentTransactionInquiryResponse.TransactionDetail> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        RentTransactionInquiryResponse.TransactionDetail::getExcluUseAr,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                details -> {
                                    double avgDeposit = details.stream()
                                            .mapToDouble(detail -> Double.parseDouble(detail.getDeposit().replace(",", "")))
                                            .average()
                                            .orElse(0);
                                    double avgMonthlyRent = details.stream()
                                            .mapToDouble(detail -> Double.parseDouble(detail.getMonthlyRent().replace(",", "")))
                                            .average()
                                            .orElse(0);
                                    int transactionCount = details.size();
                                    return new RentTransactionInquiryResponse.AverageInfo(avgDeposit, avgMonthlyRent, transactionCount);
                                }
                        )
                ));
    }
}
