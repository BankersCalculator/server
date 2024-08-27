package com.bankersCalculator.server.advice.userInputInfo.repository;

import com.bankersCalculator.server.advice.userInputInfo.domain.UserInputInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInputInfoRepository extends JpaRepository<UserInputInfo, Long> {
}
