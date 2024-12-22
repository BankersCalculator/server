package com.myZipPlan.server.common.webhook;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static ch.qos.logback.classic.util.StatusViaSLF4JLoggerFactory.addError;

public class DiscordWebhookAppender extends AppenderBase<ILoggingEvent>  {
    private String webhookUrl;
    private RestTemplate restTemplate;

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        System.out.println("DiscordWebhookAppender invoked.");
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            addError("Webhook URL is not set for DiscordWebhookAppender.");
            return;
        }

        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        // 메시지 생성
        String message = String.format("🚨 **Log Alert!**\n**Level:** %s\n**Message:** %s",
                eventObject.getLevel(),
                eventObject.getFormattedMessage());

        // JSON 페이로드 생성
        String payload = "{ \"content\": \"" + escapeForJson(message) + "\" }";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 요청 생성
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(webhookUrl, entity, String.class);
        } catch (Exception e) {
            addError("Failed to send log to Discord", e);
        }
    }

    /**
     * JSON 내에서 안전하게 문자열을 사용하기 위한 이스케이프 처리
     */
    private String escapeForJson(String text) {
        return text.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
