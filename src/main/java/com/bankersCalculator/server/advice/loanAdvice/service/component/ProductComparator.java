package com.bankersCalculator.server.advice.loanAdvice.service.component;

import com.bankersCalculator.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.BestLoanProductResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductComparator {

    /**
     *
     * // TODO: 대출상품 비교 로직 다시 검토하고 고도화 필요함.
     * 대출상품 비교
     * - 대출한도 점수: 대출한도가 높을 수록 높은 점수를 부여한다.
     * - 금리 점수: 금리가 낮을 수록 높은 점수를 부여한다.
     * - 필요금액과 보유금액 간 차이 점수: 필요금액과 대출한도의 차이가 적을 수록 높은 점수를 부여한다.
     */


    private static final BigDecimal LOAN_LIMIT_WEIGHT = new BigDecimal("0.3"); // 대출한도 점수 가중치
    private static final BigDecimal RATE_WEIGHT = new BigDecimal("0.5"); // 금리 점수 가중치
    private static final BigDecimal NEED_GAP_WEIGHT = new BigDecimal("0.2"); // 필요금액과 보유금액 간 차이 점수 가중치
    private static final BigDecimal MIN_LOAN_LIMIT = new BigDecimal("10000000"); // 천만원
    private static final BigDecimal MAX_LOAN_LIMIT = new BigDecimal("1000000000"); // 십억원

    public BestLoanProductResult compareProducts(BigDecimal rentalDeposit, List<LoanLimitAndRateResultDto> loanLimitAndRateResultDto) {
        if (loanLimitAndRateResultDto == null || loanLimitAndRateResultDto.isEmpty()) {
            return null;
        }
        if (loanLimitAndRateResultDto.size() == 1) {
            return buildOptimalResult(loanLimitAndRateResultDto.get(0));
        }

        LoanLimitAndRateResultDto bestProduct = LoanLimitAndRateResultDto.builder()
            .possibleLoanLimit(BigDecimal.ZERO)
            .expectedLoanRate(BigDecimal.ZERO)
            .build();
        BigDecimal bestScore = BigDecimal.ZERO;

        for (LoanLimitAndRateResultDto product : loanLimitAndRateResultDto) {

            if (!product.isEligible()) continue;

            BigDecimal currentScore = calculateScore(product, rentalDeposit);
            if (currentScore.compareTo(bestScore) > 0) {
                bestScore = currentScore;
                bestProduct = product;
            }
        }

        return buildOptimalResult(bestProduct);
    }

    private BigDecimal calculateScore(LoanLimitAndRateResultDto product, BigDecimal neededAmount) {
        /*
         * 대출상품의 점수 계산식
         * 점수 = 대출한도 점수 + 금리 점수 + 필요금액과 보유금액 간 차이 점수
         */
        BigDecimal loanLimitScore = calculateLoanLimitScore(product.getPossibleLoanLimit());
        BigDecimal rateScore = calculateRateScore(product.getExpectedLoanRate());
        BigDecimal needGapScore = calculateNeedGapScore(product.getPossibleLoanLimit(), neededAmount);

        return loanLimitScore.add(rateScore).add(needGapScore);
    }

    private BigDecimal calculateLoanLimitScore(BigDecimal loanLimit) {
        // 최소값과 최대값을 기준으로 0~1점 사이의 값으로 정규화한다.
        BigDecimal normalizedLimit = loanLimit.max(MIN_LOAN_LIMIT).min(MAX_LOAN_LIMIT)
            .subtract(MIN_LOAN_LIMIT)
            .divide(MAX_LOAN_LIMIT.subtract(MIN_LOAN_LIMIT), 4, RoundingMode.HALF_UP);

        return normalizedLimit.multiply(LOAN_LIMIT_WEIGHT);
    }

    private BigDecimal calculateRateScore(BigDecimal rate) {
        // 최소값과 최대값을 기준으로 0~1점 사이의 값으로 정규화한다.
        // 금리가 낮을 수록 높은 점수를 부여한다.
        BigDecimal maxRate = new BigDecimal("10"); // 최대 금리를 10%로 가정
        BigDecimal minRate = new BigDecimal("1");  // 최소 금리를 1%로 가정

        BigDecimal normalizedRate = maxRate.subtract(rate.max(minRate).min(maxRate))
            .divide(maxRate.subtract(minRate), 4, RoundingMode.HALF_UP);

        return normalizedRate.multiply(RATE_WEIGHT);
    }

    private BigDecimal calculateNeedGapScore(BigDecimal loanLimit, BigDecimal neededAmount) {
        // 대출한도와 필요금액의 차이를 최대 가능한 차이로 정규화한 후,
        // 1에서 빼서 0~1점 사이의 값으로 변환한다.
        // 필요금액과 대출한도의 차이가 적을 수록 높은 점수를 부여한다.
        BigDecimal gap = loanLimit.subtract(neededAmount).abs();
        BigDecimal maxGap = MAX_LOAN_LIMIT.subtract(MIN_LOAN_LIMIT);

        BigDecimal normalizedGap = BigDecimal.ONE.subtract(gap.divide(maxGap, 4, RoundingMode.HALF_UP));
        return normalizedGap.multiply(NEED_GAP_WEIGHT);
    }

    private BestLoanProductResult buildOptimalResult(LoanLimitAndRateResultDto product) {
        return BestLoanProductResult.builder()
            .productType(product.getProductType())
            .possibleLoanLimit(product.getPossibleLoanLimit())
            .expectedLoanRate(product.getExpectedLoanRate())
            .build();
    }

}