
package com.bankersCalculator.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BankersCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankersCalculatorApplication.class, args);
    }

}
