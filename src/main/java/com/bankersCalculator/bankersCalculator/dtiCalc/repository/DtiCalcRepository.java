package com.bankersCalculator.bankersCalculator.dtiCalc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcEntity;


@Repository
public interface DtiCalcRepository extends JpaRepository<DtiCalcEntity, Long> {
    // Custom query methods if needed
}
