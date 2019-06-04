package ru.kirienko.ordersparser.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderValidation {
    private String field;
    private String value;
    private String description;

    public OrderValidation(String field, String value) {
        this.field = field;
        this.value = value;
    }
}
