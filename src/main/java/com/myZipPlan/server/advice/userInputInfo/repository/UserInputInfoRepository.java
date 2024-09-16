package com.myZipPlan.server.advice.userInputInfo.repository;

import com.myZipPlan.server.advice.userInputInfo.entity.UserInputInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInputInfoRepository extends JpaRepository<UserInputInfo, Long> {


    Optional<UserInputInfo> findTop1ByUserIdOrderByCreatedDateTimeDesc(Long userId);

    List<UserInputInfo> findTop5ByUserIdOrderByCreatedDateTimeDesc(Long userId);

}
