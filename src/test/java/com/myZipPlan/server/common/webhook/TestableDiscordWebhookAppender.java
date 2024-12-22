package com.myZipPlan.server.common.webhook;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestableDiscordWebhookAppender extends DiscordWebhookAppender {
    @Override
    public void append(ILoggingEvent eventObject) {
        super.append(eventObject);
    }
}
