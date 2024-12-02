package com.myZipPlan.server.common.aspects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class ApiLog {

    @Builder
    public ApiLog(String endpoint, String timestamp, Long userId, String username) {
        this.endpoint = endpoint;
        this.timestamp = timestamp;
        this.userId = userId;
        this.username = username;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endpoint;
    private String timestamp;
    private Long userId;
    private String username;


}
