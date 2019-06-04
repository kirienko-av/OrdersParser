package ru.kirienko.ordersparser.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Order {
    private Long id;
    private BigDecimal amount;
    private String currency;
    private String comment;
    private String fileName;
    private Long line;
    private String result;

    public String toString() {
        final String pattern = "{\"id\":%s, \"amount\":%s, \"comment\":\"%s\", \"filename\":\"%s\", \"line\":%s, \"result\":\"%s\" }";
        Map<String, String> validations = getErrorResults();

        return String.format(pattern, Optional.ofNullable(id)
                .map(Object::toString)
                .orElse(validations.get("id")),
                Optional.ofNullable(amount)
                        .map(Object::toString)
                        .orElse(validations.get("amount")),
                Optional.ofNullable(comment)
                        .orElse(validations.get("comment")),
                Optional.ofNullable(fileName)
                        .orElse(validations.get("filename")),
                Optional.ofNullable(line)
                        .map(Object::toString)
                        .orElse(validations.get("line")),
                Optional.ofNullable(result)
                        .orElse(validations.get("result"))
        );
    }

    private Map<String, String> getErrorResults(){
        final ObjectMapper objectMapper = new ObjectMapper();
        Set<OrderValidation> validations = new HashSet<>();

        Optional.ofNullable(result)
                .filter(r -> !r.equals("OK"))
                .filter(r -> !r.contains("FORMAT ERROR"))
                .ifPresent(r -> {
                    try {
                        validations.addAll(objectMapper.readValue(r, new TypeReference<Set<OrderValidation>>() {
                        }));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        return validations.stream()
                .collect(Collectors.toMap(OrderValidation::getField, OrderValidation::getValue));

    }
}
