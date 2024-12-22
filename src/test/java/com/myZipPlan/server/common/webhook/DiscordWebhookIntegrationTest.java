package com.myZipPlan.server.common.webhook;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
//@TestPropertySource(properties = "discord.webhook.url=https://discord.com/api/webhooks/1316396683260592178/zEYr2-WexcpVQ7U-BmQknm1LdeLvzSryM-t1dBI0PCh-eUcZCjqpSutqBBmnBKEpGXMF")
public class DiscordWebhookIntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(DiscordWebhookIntegrationTest.class);

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    @Test
    void testSendErrorLogToDiscord() {
        logger.info("webhookUrl: {}", webhookUrl);
        // TestDiscordWebhookAppender 인스턴스 생성
        TestableDiscordWebhookAppender discordWebhookAppender = new TestableDiscordWebhookAppender();
        discordWebhookAppender.setWebhookUrl(webhookUrl);

        // 로그 이벤트를 생성하고 다양한 예외 메시지로 append 호출
        simulateDatabaseError(discordWebhookAppender);
        simulateNullPointerError(discordWebhookAppender);
        simulateFileNotFoundError(discordWebhookAppender);
    }

    private void simulateDatabaseError(TestableDiscordWebhookAppender discordWebhookAppender) {
        try {
            // 예외 발생
            throw new RuntimeException("Database connection failed!");
        } catch (Exception e) {
            // 로그 이벤트 생성
            LoggingEvent loggingEvent = new LoggingEvent();
            loggingEvent.setLevel(Level.ERROR);
            loggingEvent.setMessage("(Test)A database error occurred: " + e.toString());
            loggingEvent.setThreadName(Thread.currentThread().getName());

            // DiscordWebhookAppender로 로그 전송
            discordWebhookAppender.append(loggingEvent);
        }
    }

    private void simulateNullPointerError(TestableDiscordWebhookAppender discordWebhookAppender) {
        try {
            // NullPointerException 발생
            String value = null;
            value.length(); // NullPointerException 발생
        } catch (Exception e) {
            // 로그 이벤트 생성
            LoggingEvent loggingEvent = new LoggingEvent();
            loggingEvent.setLevel(Level.ERROR);
            loggingEvent.setThreadName(Thread.currentThread().getName());
            loggingEvent.setMessage("(Test)A null pointer exception occurred: " + e.toString());

            // DiscordWebhookAppender로 로그 전송
            discordWebhookAppender.append(loggingEvent);
        }
    }

    private void simulateFileNotFoundError(TestableDiscordWebhookAppender discordWebhookAppender) {
        try {
            // FileNotFoundException 발생
            throw new java.io.FileNotFoundException("Configuration file not found!");
        } catch (Exception e) {
            // 로그 이벤트 생성
            LoggingEvent loggingEvent = new LoggingEvent();
            loggingEvent.setLevel(Level.ERROR);
            loggingEvent.setMessage("(Test)A file handling error occurred: " + e.toString());
            loggingEvent.setThreadName(Thread.currentThread().getName());


            // DiscordWebhookAppender로 로그 전송
            discordWebhookAppender.append(loggingEvent);
        }
    }
}
