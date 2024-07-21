package com.bankersCalculator.server.dtiCalc.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.common.enums.RepaymentType;

//DTO 변환 테스트
@SpringBootTest
public class DtiCalcRequestTest {
	@Test
    public void testToServiceRequest() {
        DtiCalcRequest request = new DtiCalcRequest();
        request.setAnnualIncome(1000000);
        
        DtiCalcRequest.LoanStatus loanStatus = new DtiCalcRequest.LoanStatus();
        DtiCalcRequest.LoanStatus loanStatus2 = new DtiCalcRequest.LoanStatus();
        loanStatus.setRepaymentType(RepaymentType.BULLET);  //BULLET AMORTIZING EQUAL_PRINCIPAL
        loanStatus.setLoanType(LoanType.MORTGAGE);
        loanStatus.setMaturityPaymentAmount(60000000);
        loanStatus.setPrincipal(5000000);
        loanStatus.setTerm(300);
        loanStatus.setGracePeriod(120);
        loanStatus.setInterestRatePercentage(3);
        request.getLoanStatusList().add(loanStatus);
        
        loanStatus2.setRepaymentType(RepaymentType.BULLET);  //BULLET AMORTIZING EQUAL_PRINCIPAL
        loanStatus2.setLoanType(LoanType.MORTGAGE);
        loanStatus2.setMaturityPaymentAmount(60000000);
        loanStatus2.setPrincipal(5000000);
        loanStatus2.setTerm(300);
        loanStatus2.setGracePeriod(120);
        loanStatus2.setInterestRatePercentage(2);
        request.getLoanStatusList().add(loanStatus2);
        

        DtiCalcServiceRequest serviceRequest = request.toServiceRequest();
        System.out.println("serviceRequest.getLoanStatusList().size() : " + serviceRequest.getLoanStatusList().size());
        System.out.println("serviceRequest.getLoanStatusList().get(0).getRepaymentType() : " + serviceRequest.getLoanStatusList().get(0).getRepaymentType());
        System.out.println("serviceRequest.getLoanStatusList().get(0).getLoanType() : " + serviceRequest.getLoanStatusList().get(0).getLoanType());
        System.out.println("serviceRequest.getLoanStatusList().get(0).getInterestRate() : " + serviceRequest.getLoanStatusList().get(0).getInterestRate());
        System.out.println("serviceRequest.getLoanStatusList().get(1).getInterestRate() : " + serviceRequest.getLoanStatusList().get(1).getInterestRate());
        
        assertEquals(RepaymentType.BULLET, serviceRequest.getLoanStatusList().get(0).getRepaymentType());
        assertEquals(LoanType.MORTGAGE, serviceRequest.getLoanStatusList().get(0).getLoanType());
        assertEquals(60000000, serviceRequest.getLoanStatusList().get(0).getMaturityPaymentAmount());
        assertEquals(5000000, serviceRequest.getLoanStatusList().get(0).getPrincipal());
        assertEquals(300, serviceRequest.getLoanStatusList().get(0).getTerm());
        assertEquals(120, serviceRequest.getLoanStatusList().get(0).getGracePeriod());
        assertEquals(0.03, serviceRequest.getLoanStatusList().get(0).getInterestRate());
        assertEquals(2, serviceRequest.getLoanStatusList().size());
        assertEquals(0.02, serviceRequest.getLoanStatusList().get(1).getInterestRate());
        
        // Enum의 각 값이 예상대로 정의되었는지 테스트합니다.
        RepaymentType bullet = RepaymentType.BULLET;
        RepaymentType amortizing = RepaymentType.AMORTIZING;
        RepaymentType equalPrincipal = RepaymentType.EQUAL_PRINCIPAL;

        // 각 Enum 값의 설명이 예상대로 설정되었는지 확인합니다.
        assertEquals("일시상환", bullet.getDescription());
        assertEquals("원리금균등분할상환", amortizing.getDescription());
        assertEquals("원금균등분할상환", equalPrincipal.getDescription());
   
        //  Enum의 valueOf 메서드가 예상대로 작동하는지 테스트합니다.
        assertEquals(RepaymentType.BULLET, RepaymentType.valueOf("BULLET"));
        assertEquals(RepaymentType.AMORTIZING, RepaymentType.valueOf("AMORTIZING"));
        assertEquals(RepaymentType.EQUAL_PRINCIPAL, RepaymentType.valueOf("EQUAL_PRINCIPAL"));
        
        // Enum의 설명이 예상대로 작동하는지 테스트합니다
        for (RepaymentType type : RepaymentType.values()) {
            switch (type) {
                case BULLET:
                    assertEquals("일시상환", type.getDescription());
                    break;
                case AMORTIZING:
                    assertEquals("원리금균등분할상환", type.getDescription());
                    break;
                case EQUAL_PRINCIPAL:
                    assertEquals("원금균등분할상환", type.getDescription());
                    break;
            }
        }
    }
}
