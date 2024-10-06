package com.myZipPlan.server.common.enums.loanAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BaseRate {

    /**
     * COFIX : 은행연합회 https://portal.kfb.or.kr/fingoods/cofix.php
     *
     *
     * CD3개월물 : 금융투자협회 - CD수익률 https://www.kofiabond.or.kr
     *
     * 금융채 : 금융투자협회 - 시가평가 https://www.kofiabond.or.kr
     */

    COFIX_NEW_DEAL("신규취급액기준 COFIX"),
    COFIX_BALANCE("잔액기준 COFIX"),
    COFIX_NEW_BALANCE("신잔액기준 COFIX"),
    COFIX_SHORT_TERM("단기 COFIX"),

    // CD 금리
    CD_91("CD 91일물"),

    // 금융채 금리
    FINANCIAL_BOND_3M("금융채 3개월물"),
    FINANCIAL_BOND_6M("금융채 6개월물"),
    FINANCIAL_BOND_12M("금융채 12개월물"),
    FINANCIAL_BOND_24M("금융채 24개월물");

    private final String description;


}
