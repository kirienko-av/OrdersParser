package ru.kirienko.ordersparser.parser.csv;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class OrderCsvFormat {
    @JsonProperty("идентификатор ордера")
    private String id;
    @JsonProperty("сумма")
    private String amount;
    @JsonProperty("валюта")
    private String currency;
    @JsonProperty("комментарий")
    private String comment;
}