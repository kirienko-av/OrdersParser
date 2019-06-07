package ru.kirienko.ordersparser.parser.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class OrderMixIn {
    @JsonProperty("orderId")
    String id;
    @JsonProperty("amount")
    String amount;
    @JsonProperty("currency")
    String currency;
    @JsonProperty("comment")
    String comment;
}
