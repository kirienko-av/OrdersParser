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

    @Override
    public String toString() {
        return "{id:" + id + "," +
                "amount:" + amount + "," +
                "currency:\"" + currency + "\"," +
                "comment:\"" + comment + "\"," +
                "filename:\"" + fileName + "\"," +
                "line:" + line + "," +
                "result:\"" + result + "\"}";
    }
}
