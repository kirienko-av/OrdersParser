package ru.kirienko.ordersparser.service;

import ru.kirienko.ordersparser.domain.OrderValidation;
import ru.kirienko.ordersparser.integration.OrderItemReader;

public interface OrderService {
    OrderItemReader getOrderItemReaderByFileType(String fileType) throws Exception;

    OrderValidation validation(String fieldName, String value);
}
