package com.myZipPlan.server.common.aspects;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
}
