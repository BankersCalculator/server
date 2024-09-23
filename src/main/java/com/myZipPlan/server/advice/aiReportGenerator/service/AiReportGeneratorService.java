package com.myZipPlan.server.advice.aiReportGenerator.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class AiReportGeneratorService {

    private final OpenAiApiService openAiApiService;

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
