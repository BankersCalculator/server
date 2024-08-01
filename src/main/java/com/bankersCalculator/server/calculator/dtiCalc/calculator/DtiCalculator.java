package com.bankersCalculator.server.calculator.dtiCalc.calculator;

import com.bankersCalculator.server.calculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;

/*
 * 인터페이스 (Interface): 인터페이스는 클래스가 구현해야 하는 메서드들의 집합을 정의합니다. 인터페이스는 메서드의 시그니처(반환 타입, 메서드 이름, 매개변수 타입)를 정의하며, 실제 구현은 포함하지 않습니다.
 */
public interface DtiCalculator {
    //Spring Framework에서 의존성 주입(Dependency Injection)을 위해 사용하는 어노테이션입니다. 이 어노테이션을 사용하면 스프링 컨테이너가 자동으로 해당 필드, 생성자, 또는 메서드에 필요한 의존성을 주입합니다. 이는 Spring의 IoC(Inversion of Control) 컨테이너가 관리하는 빈(Bean)들 사이의 의존성을 설정하는 데 사용됩니다.
    DtiCalcResult calculateDti(DtiCalcServiceRequest.LoanStatus loanStatus);
}
