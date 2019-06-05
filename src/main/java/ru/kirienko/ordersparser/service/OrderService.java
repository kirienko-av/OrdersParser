package ru.kirienko.ordersparser.service;

import ru.kirienko.ordersparser.domain.OrderValidation;
import ru.kirienko.ordersparser.reader.OrderItemReader;

public interface OrderService {
    OrderItemReader getOrderItemReaderByFileType(String fileType) throws Exception;

    OrderValidation validation(String fieldName, String value);
}
