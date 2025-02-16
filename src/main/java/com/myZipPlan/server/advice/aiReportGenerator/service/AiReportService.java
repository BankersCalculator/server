package com.myZipPlan.server.advice.aiReportGenerator.service;


import com.myZipPlan.server.advice.loanAdvice.dto.internal.AdditionalInformation;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.BestLoanProductResult;
import com.myZipPlan.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import com.myZipPlan.server.advice.userInputInfo.entity.UserInputInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class AiReportService {

    private final OpenAiApiService openAiApiService;

    public String generateAiReport(UserInputInfo userInputInfo, BestLoanProductResult bestProduct,
                                    AdditionalInformation additionalInfo, List<RecommendedProductDto> recommendedProducts) {

        StringBuilder promptInfoBuilder = new StringBuilder();
        promptInfoBuilder.append("사용자 입력 정보: ").append(userInputInfo.toString()).append("\n")
            .append("최적 상품 정보: ").append(bestProduct.toString()).append("\n")
            .append("추가 정보: ").append(additionalInfo.toString()).append("\n")
            .append("기타 추천 상품 목록: \n ");
        recommendedProducts.forEach(recommendedProduct ->
            promptInfoBuilder.append(recommendedProduct.toString()).append("\n"));

        String promptInfoData = promptInfoBuilder.toString();

        return generateAiReport(promptInfoData);
    }

    public String generateAiReport(String promptInfoData) {

        log.info("AI 보고서 생성 요청: {}", promptInfoData);
        String prompt = OpenAiPrompt.generatePrompt(promptInfoData);

        String response = "";

        try {
            response = openAiApiService.callGpt4oApi(prompt);
        } catch (Exception e) {
            System.out.println(e);
        }

            return response;
        }
    }
