package ru.kirienko.ordersparser.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.util.List;

@MessagingGateway
public interface OrderParserGateway {
    @Gateway(requestChannel = "filePathsChannel")
    void parse(List<String> filePaths);
}
