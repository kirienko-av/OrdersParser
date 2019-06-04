package ru.kirienko.ordersparser.domain;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class Order {
    private Long id;
    private BigDecimal amount;
    private String currency;
    private String comment;
    private String fileName;
    private Long line;
    private String result;
}
