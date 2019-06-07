package ru.kirienko.ordersparser.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private String id;
    private String amount;
    private String currency;
    private String comment;
    private String fileName;
    private Long line;
    private String result;
}
