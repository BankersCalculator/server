package com.myZipPlan.server.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.junit.jupiter.api.*;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DTOTest {

    private ObjectMapper objectMapper;
    private CamelDTO camelDTO;
    private CamelWithJsonPropertyDTO camelWithJsonPropertyDTO;
    private UpperSnakeDTO upperSnakeDTO;


    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void forCaching() throws Exception {
        camelDTO = createCamelDTO();
        camelWithJsonPropertyDTO = createCamelWithJsonPropertyDTO();
        upperSnakeDTO = createUpperSnakeDTO();

        long start = System.nanoTime();
        String json3 = objectMapper.writeValueAsString(camelDTO);
        String json = objectMapper.writeValueAsString(camelWithJsonPropertyDTO);
        String json2 = objectMapper.writeValueAsString(upperSnakeDTO);
        long end = System.nanoTime();

        System.out.println("caching: " + (end - start) / 1_000_000.0 + " ms");
    }

    @Test
    @Order(2)
    @DisplayName("PropertyNamingStrategy 를 활용한 Camel -> UpperSnake")
    public void WithPropertyNamingStrategy() throws Exception {

        ArrayList<Object> objects = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            camelDTO = createCamelDTO();
            objects.add(camelDTO);
        }
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_SNAKE_CASE);

        long start = System.nanoTime();
        String json = objectMapper.writeValueAsString(objects);
        long end = System.nanoTime();

        System.out.println("WithPropertyNamingStrategy (CAMEL -> SNAKE_CASE): " + (end - start) / 1_000_000.0 + " ms");
        assertNotNull(json);
        System.out.println(json);
    }

//    @Test
//    @Order(3)
//    @DisplayName("@JsonProperty 를 활용한 Camel -> UpperSnake")
//    public void WithCamelToSnakeTransformation() throws Exception {
//
//        ArrayList<Object> objects = new ArrayList<>();
//        for (int i=0; i<30000; i++){
//            camelWithJsonPropertyDTO = createCamelWithJsonPropertyDTO();
//            objects.add(camelWithJsonPropertyDTO);
//        }
//
//        long start = System.nanoTime();
//        String json = objectMapper.writeValueAsString(objects);
//        long end = System.nanoTime();
//
//        System.out.println("With Transformation: " + (end - start) / 1_000_000.0 + " ms");
//        assertNotNull(json);
//        System.out.println(json);
//    }

    @Test
    @Order(4)
    @DisplayName("UpperSnake -> UpperSnake 그러나 자동으로 lower 됨")
    public void WithoutTransformation() throws Exception {
        ArrayList<Object> objects = new ArrayList<>();
        for (int i=0; i<10000; i++){
            upperSnakeDTO = createUpperSnakeDTO();
            objects.add(upperSnakeDTO);
        }

        long start = System.nanoTime();
        String json = objectMapper.writeValueAsString(objects);
        long end = System.nanoTime();

        System.out.println("Without Transformation: " + (end - start) / 1_000_000.0 + " ms");
        assertNotNull(json);
        System.out.println(json);
    }

    @Test
    @Order(5)
    @DisplayName("PropertyNamingStrategy 를 활용한 Upper -> Upper")
    public void WithUpperToUpperTransformation() throws Exception {
        ArrayList<Object> objects = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            upperSnakeDTO = createUpperSnakeDTO();
            objects.add(upperSnakeDTO);
        }

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_SNAKE_CASE);

        long start = System.nanoTime();
        String json = objectMapper.writeValueAsString(objects);
        long end = System.nanoTime();

        System.out.println("WithUpperToUpperTransformation (UPPER -> UPPER): " + (end - start) / 1_000_000.0 + " ms");
        assertNotNull(json);
        System.out.println(json);
    }


    public CamelWithJsonPropertyDTO createCamelWithJsonPropertyDTO() {
        CamelWithJsonPropertyDTO dto = CamelWithJsonPropertyDTO.builder()
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

    // UpperSnake DTO용 동적 필드 설정 메서드
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