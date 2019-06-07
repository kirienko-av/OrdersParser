package ru.kirienko.ordersparser.parser;

import ru.kirienko.ordersparser.dto.OrderDTO;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface OrderParser {
    Stream<OrderDTO> getOrderStream(Path filePath);
}
