package ru.kirienko.ordersparser.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.kirienko.ordersparser.domain.Order;

import java.util.List;

@MessagingGateway
public interface OrderParserGateway {
    @Gateway(requestChannel = "filesChannel")
    void parse(List<String> filePaths);

    @Gateway(requestChannel = "printChannel")
    void print(Order order);
}
