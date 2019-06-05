package ru.kirienko.ordersparser.integration;

import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kirienko.ordersparser.domain.Order;
import ru.kirienko.ordersparser.domain.OrderLine;

public abstract class OrderItemReader {

    @Autowired
    private OrderParserGateway orderParserGateway;

    abstract FlatFileItemReader<Order> itemReader(String filePath);

    abstract Order lineMapper(OrderLine orderLine);

    public final ItemStreamReader<Order> getItemReader(String filePath) {
        FlatFileItemReader<Order> reader = itemReader(filePath);
        reader.setLineMapper((line, lineNumber) -> {
            Order order = lineMapper(new OrderLine(line, lineNumber, FilenameUtils.getName(filePath)));
            orderParserGateway.print(order);
            return order;
        });
        return reader;
    }
}
