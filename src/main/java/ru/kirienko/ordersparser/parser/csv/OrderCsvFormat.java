package ru.kirienko.ordersparser.parser.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "amount", "currency", "comment"})
public abstract class OrderCsvFormat {
    @JsonProperty("id")
    private String id;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("comment")
    private String comment;
}