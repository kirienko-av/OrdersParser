package ru.kirienko.ordersparser.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderLine {
    private String line;
    private Integer lineNumber;
    private String fileName;
}
