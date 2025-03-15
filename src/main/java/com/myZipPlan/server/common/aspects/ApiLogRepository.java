package com.myZipPlan.server.common.aspects;

import com.myZipPlan.server.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {

    @Modifying
    @Query("UPDATE ApiLog u SET u.userId = :newUser WHERE u.userId = :guest")
    void updateUserFromGuest(@Param("guest") Long guest, @Param("newUser") Long newUser);
}
