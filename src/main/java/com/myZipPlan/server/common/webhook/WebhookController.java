package com.myZipPlan.server.common.webhook;

import com.myZipPlan.server.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/webhook")
@RestController
@Slf4j
public class WebhookController {
    @GetMapping("/github-actions/health-check")
    public ApiResponse<Map<String, String>> gitHubActionsHealthCheck() {
        log.debug("Github Actions health check called");
        Map<String, String> healthCheckResponse = new HashMap<>();
        healthCheckResponse.put("status", "ok");
        healthCheckResponse.put("timestamp", LocalDateTime.now().toString());
        return ApiResponse.ok(healthCheckResponse);
    }
}
