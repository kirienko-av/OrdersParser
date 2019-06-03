package ru.kirienko.ordersparser.service;

import ru.kirienko.ordersparser.integration.OrderItemReader;

public interface OrderItemReaderService {
    OrderItemReader getOrderItemReaderByFileType(String fileType) throws Exception;
}
