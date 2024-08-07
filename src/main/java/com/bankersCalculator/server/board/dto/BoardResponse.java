package com.bankersCalculator.server.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String createdDate;
    private String modifiedDate;
}