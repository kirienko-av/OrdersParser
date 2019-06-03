package ru.kirienko.ordersparser.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Order {
    private Long id;
    private Long amount;
    private String currency;
    private String comment;
    private String fileName;
    private Long line;
    private String result;
}
