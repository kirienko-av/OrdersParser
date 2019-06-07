package ru.kirienko.ordersparser.parser.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class OrderMixIn {
    @JsonProperty("orderId")
    private String id;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("comment")
    private String comment;
}
