package ru.kirienko.ordersparser.integration;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import ru.kirienko.ordersparser.configuration.FileType;
import ru.kirienko.ordersparser.domain.Order;
import ru.kirienko.ordersparser.domain.OrderLine;

@FileType("json")
@Component
public class JsonOrderItemReader extends OrderItemReader {
    private final ModelMapper modelMapper;

    @Autowired
    public JsonOrderItemReader(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public FlatFileItemReader<Order> itemReader(String filePath) {
        FlatFileItemReader<Order> reader = new FlatFileItemReader<>();
        final FileSystemResource fileResource = new FileSystemResource(filePath);
        reader.setResource(fileResource);
        return reader;
    }

    @Override
    public Order lineMapper(OrderLine orderLine){
        return modelMapper.map(orderLine, Order.class, "json");
    }
}
