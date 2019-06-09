package ru.kirienko.ordersparser.service;

import ru.kirienko.ordersparser.domain.Order;

import java.util.stream.Stream;

public interface ParserService {
    Stream<Order> lines(String filePath);

    Order validate(Order order);
}
