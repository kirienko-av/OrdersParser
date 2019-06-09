package ru.kirienko.ordersparser.parser;

import ru.kirienko.ordersparser.domain.Order;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface OrderParser {
    Stream<Order> lines(Path filePath);
}
