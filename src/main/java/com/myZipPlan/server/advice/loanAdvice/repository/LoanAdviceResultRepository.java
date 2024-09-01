package com.myZipPlan.server.advice.loanAdvice.repository;

import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanAdviceResultRepository extends JpaRepository<LoanAdviceResult, Long> {

}
