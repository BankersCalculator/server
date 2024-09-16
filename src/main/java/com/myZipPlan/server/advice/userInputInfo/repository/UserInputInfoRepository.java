package com.myZipPlan.server.advice.userInputInfo.repository;

import com.myZipPlan.server.advice.userInputInfo.entity.UserInputInfo;
import com.myZipPlan.server.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInputInfoRepository extends JpaRepository<UserInputInfo, Long> {


    Optional<UserInputInfo> findTop1ByUserIdOrderByCreatedDateTimeDesc(Long userId);

    List<UserInputInfo> findTop5ByUserIdOrderByCreatedDateTimeDesc(Long userId);


    @Modifying
    @Query("UPDATE UserInputInfo u SET u.user = :newUser WHERE u.user = :tempUser")
    void updateUserFromTempUser(@Param("tempUser") User tempUser, @Param("newUser") User newUser);

}
