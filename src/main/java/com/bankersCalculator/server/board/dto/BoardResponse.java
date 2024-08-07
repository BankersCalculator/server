package com.bankersCalculator.server.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDate createdDate;
    private LocalDate modifiedDate;
}