package com.bankersCalculator.server.advise.loanAdvise.domain;

import com.bankersCalculator.server.common.enums.loanAdvise.RentalType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "rental_cost")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RentalType rentalType; // 임차타입

    private long rentalDeposit; // 임차보증금

    private long monthlyRent; // 월세

    @ManyToOne
    @JoinColumn(name = "user_input_info_id")
    private UserInputInfo userInputInfo;
}