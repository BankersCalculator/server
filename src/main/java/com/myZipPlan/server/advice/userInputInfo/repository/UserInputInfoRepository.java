package com.myZipPlan.server.advice.userInputInfo.repository;

import com.myZipPlan.server.advice.userInputInfo.domain.UserInputInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInputInfoRepository extends JpaRepository<UserInputInfo, Long> {
}
