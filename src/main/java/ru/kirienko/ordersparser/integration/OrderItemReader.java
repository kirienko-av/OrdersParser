package ru.kirienko.ordersparser.integration;

import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import ru.kirienko.ordersparser.domain.Order;
import ru.kirienko.ordersparser.domain.OrderLine;

public interface OrderItemReader {
    FlatFileItemReader<Order> itemReader(String filePath);

    Order lineMapper(OrderLine orderLine);

    default ItemStreamReader<Order> getItemReader(String filePath){
        FlatFileItemReader<Order> reader = itemReader(filePath);
        reader.setLineMapper((line, lineNumber) -> {
                Order order = lineMapper(new OrderLine(line, lineNumber, FilenameUtils.getName(filePath)));
                System.out.println(order);
                return order;
            });
        return reader;
    }
}
