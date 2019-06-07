package ru.kirienko.ordersparser.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    private Long id;
    private BigDecimal amount;
    private String currency;
    private String comment;
    private String fileName;
    private Long line;
    private String result;
}
