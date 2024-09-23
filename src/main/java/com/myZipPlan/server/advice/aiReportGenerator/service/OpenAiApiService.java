package com.myZipPlan.server.advice.aiReportGenerator.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiApiService {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    @Value("${spring.ai.openai.api-key}")
    private String API_KEY;


    //
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenAiApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String callGpt4oApi(String prompt) throws Exception {
        ObjectNode requestBody = objectMapper.createObjectNode();

        ArrayNode messages = requestBody.putArray("messages");
        requestBody.put("model", "gpt-4o");

        ObjectNode systemMessage = messages.addObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "유저에게 제공할 보고서야. 깔끔하고 정돈되게 써줘.");
        ObjectNode userMessage = messages.addObject();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);


        String requestBodyStr = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(API_URL))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(requestBodyStr))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("GPT 통신 실패" + response.toString());
        }

        ObjectNode responseJson = objectMapper.readValue(response.body(), ObjectNode.class);
        String text = responseJson.path("choices")
            .get(0).path("message")
            .path("content").asText();
        String trimmedText = text.replaceAll("json", "")
            .replaceAll("^```", "")
            .replaceAll("```$", "");

        log.info(trimmedText);
        return trimmedText;
    }
}
