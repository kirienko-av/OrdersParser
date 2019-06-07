package ru.kirienko.ordersparser.parser;

import ru.kirienko.ordersparser.dto.OrderDTO;

import java.util.stream.Stream;

public interface OrderParser {
    Stream<OrderDTO> getOrderStream(String filePath);
}
