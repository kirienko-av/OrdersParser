package ru.kirienko.ordersparser.service;

import ru.kirienko.ordersparser.dto.OrderDTO;

import java.util.stream.Stream;

public interface ParserService {
    Stream<OrderDTO> getOrderStream(String filePath);
}
