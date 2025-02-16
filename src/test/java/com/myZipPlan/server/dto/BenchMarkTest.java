package com.myZipPlan.server.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.junit.jupiter.api.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchMarkTest {

    private ObjectMapper objectMapper;
    ArrayList<Object> camelDtos = new ArrayList<>();
    ArrayList<Object> upperSnakeDtos = new ArrayList<>();

    public static void main(String[] args) throws IOException, RunnerException {
        Options opt = new OptionsBuilder()
            .include(BenchMarkTest.class.getSimpleName())
            .warmupIterations(10)           // 사전 테스트 횟수
            .measurementIterations(30)      // 실제 측정 횟수
            .forks(1)
            .build();
        new Runner(opt).run();                  // 벤치마킹 시작

    }

    @Setup
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_SNAKE_CASE);

        for (int i = 0; i < 10000; i++) {
            camelDtos.add(createCamelDTO());
            upperSnakeDtos.add(createUpperSnakeDTO());
        }
    }

    @Benchmark
    public void WithPropertyNamingStrategy() throws Exception {
        String json = objectMapper.writeValueAsString(camelDtos);
    }

    @Benchmark
    public void WithUpperToUpperTransformation() throws Exception {
        String json = objectMapper.writeValueAsString(upperSnakeDtos);
    }


    public CamelDTO createCamelDTO() {
        CamelDTO dto = CamelDTO.builder()
            .acctNo("ACC")
            .userName("User")
            .balance(10000L)
            .bankName("Bank")
            .branchCode("BR")
            .currency("KRW")
            .accountType("Type")
            .interestRate(2.5)
            .openDate("2024-01-01")
            .lastTransactionDate("2024-02-01")
            .isActive(true)
            .overdraftLimit(50000L)
            .creditScore(750)
            .debitCardNumber("DC")
            .creditCardNumber("CC")
            .monthlyDeposit(20000L)
            .monthlyWithdrawal(15000L)
            .yearlyInterest(5000.0)
            .loanAmount(30000L)
            .loanStatus("Active")
            .taxRate(10.0)
            .insurancePlan("PlanA")
            .transactionHistory("Hist")
            .build();

        setCamelCaseDynamicFields(dto);
        return dto;
    }

    public UpperSnakeDTO createUpperSnakeDTO() {
        UpperSnakeDTO dto = UpperSnakeDTO.builder()
            .ACCT_NO("ACC")
            .USER_NAME("User")
            .BALANCE(10000L)
            .BANK_NAME("Bank")
            .BRANCH_CODE("BR")
            .CURRENCY("KRW")
            .ACCOUNT_TYPE("Type")
            .INTEREST_RATE(2.5)
            .OPEN_DATE("2024-01-01")
            .LAST_TRANSACTION_DATE("2024-02-01")
            .IS_ACTIVE(true)
            .OVERDRAFT_LIMIT(50000L)
            .CREDIT_SCORE(750)
            .DEBIT_CARD_NUMBER("DC")
            .CREDIT_CARD_NUMBER("CC")
            .MONTHLY_DEPOSIT(20000L)
            .MONTHLY_WITHDRAWAL(15000L)
            .YEARLY_INTEREST(5000.0)
            .LOAN_AMOUNT(30000L)
            .LOAN_STATUS("Active")
            .TAX_RATE(10.0)
            .INSURANCE_PLAN("PlanA")
            .TRANSACTION_HISTORY("Hist")
            .build();

        setSnakeCaseDynamicFields(dto);
        return dto;
    }

    private void setCamelCaseDynamicFields(Object dto) {
        try {
            for (int i = 1; i <= 77; i++) {
                String letterPair = getLetterPairCamel(i);
                String methodName = "setField" + letterPair; // 예: setFieldAa, setFieldAb, ...
                Method method = dto.getClass().getMethod(methodName, String.class);
                method.invoke(dto, "F" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSnakeCaseDynamicFields(Object dto) {
        try {
            for (int i = 1; i <= 77; i++) {
                String letterPair = getLetterPairUpper(i);
                String methodName = "setFIELD_" + letterPair; // 예: setFIELD_AA, setFIELD_AB, ...
                Method method = dto.getClass().getMethod(methodName, String.class);
                method.invoke(dto, "F" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLetterPairCamel(int i) {
        int index = i - 1;
        char first = (char) ('A' + (index / 26));       // 대문자
        char second = (char) ('a' + (index % 26));       // 소문자
        return "" + first + second;
    }

    private String getLetterPairUpper(int i) {
        int index = i - 1;
        char first = (char) ('A' + (index / 26));       // 대문자
        char second = (char) ('A' + (index % 26));       // 대문자
        return "" + first + second;
    }
}