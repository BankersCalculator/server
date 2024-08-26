package com.bankersCalculator.server.advice.loanAdvice.repository;

import com.bankersCalculator.server.advice.loanAdvice.entity.LoanAdviceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanAdviceResultRepository extends JpaRepository<LoanAdviceResult, Long> {

}
