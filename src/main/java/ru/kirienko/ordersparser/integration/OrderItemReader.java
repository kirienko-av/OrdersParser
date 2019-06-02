package ru.kirienko.ordersparser.integration;

import org.springframework.batch.item.ItemStreamReader;

public interface OrderItemReader {
    ItemStreamReader<String> getItemReader(String filePath);
}
