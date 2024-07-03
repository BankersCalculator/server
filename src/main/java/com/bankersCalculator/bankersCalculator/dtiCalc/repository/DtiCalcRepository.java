package com.bankersCalculator.bankersCalculator.dtiCalc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DtiCalcRepository extends JpaRepository<DtiCalcEntity, Long> {
    //레포지토리에 정부 규제 등에 대한 값 데이터를 저장해둘지... 
	//각 대출별 이자 계산 위한 이자율 가정치 (ex. 금융채 5년물 )  
}