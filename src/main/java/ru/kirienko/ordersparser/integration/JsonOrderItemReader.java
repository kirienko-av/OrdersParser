package ru.kirienko.ordersparser.integration;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import ru.kirienko.ordersparser.configuration.FileType;
import ru.kirienko.ordersparser.domain.Order;
import ru.kirienko.ordersparser.domain.OrderLine;

@FileType("json")
@Component
public class JsonOrderItemReader implements OrderItemReader {
    private final ModelMapper modelMapper;

    @Autowired
    public JsonOrderItemReader(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ItemStreamReader<String> getItemReader(String filePath) {
            FlatFileItemReader<String> reader = new FlatFileItemReader<>();
            final FileSystemResource fileResource = new FileSystemResource(filePath);
            reader.setResource(fileResource);
            reader.setLineMapper((line, lineNumber) ->  modelMapper.map(new OrderLine(line, lineNumber, filePath), Order.class, "json").toString());
            return reader;
    }
}
