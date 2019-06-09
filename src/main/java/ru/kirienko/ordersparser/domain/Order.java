package ru.kirienko.ordersparser.domain;

import lombok.Data;

@Data
public class Order {
    private String id;
    private String amount;
    private String currency;
    private String comment;
    private String fileName;
    private Long line;
    private String result;
}
