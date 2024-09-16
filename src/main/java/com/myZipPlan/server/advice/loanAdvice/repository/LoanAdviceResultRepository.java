package com.myZipPlan.server.advice.loanAdvice.repository;

import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanAdviceResultRepository extends JpaRepository<LoanAdviceResult, Long> {

    List<LoanAdviceResult> findTop20ByUserIdOrderByCreatedDateTimeDesc(Long userId);


    @Modifying
    @Query("UPDATE LoanAdviceResult u SET u.user = :newUser WHERE u.user = :tempUser")
    void updateUserFromTempUser(@Param("tempUser") User tempUser, @Param("newUser") User newUser);

}


