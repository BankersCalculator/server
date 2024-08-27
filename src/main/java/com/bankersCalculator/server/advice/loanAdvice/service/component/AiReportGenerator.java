package com.bankersCalculator.server.advice.loanAdvice.service.component;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AiReportGenerator {

    public String generateAiReport() {

        // TODO: GPT4.0o 호출해서 AI 리포트 생성할 것.
        return "AI가 생성해 줄 보고서입니다.";
    }
}
