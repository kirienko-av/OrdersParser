package ru.kirienko.ordersparser.parser.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kirienko.ordersparser.annotation.FileType;
import ru.kirienko.ordersparser.dto.OrderDTO;
import ru.kirienko.ordersparser.parser.OrderParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Component
@FileType("csv")
public class CsvOrderParser implements OrderParser {

    @Autowired
    private CsvMapper csvMapper;

    @Override
    public Stream<OrderDTO> getOrderStream(Path filePath) {
        AtomicLong i = new AtomicLong(0);
        Stream<OrderDTO> stream = Stream.empty();
        try {
            stream = Files.lines(filePath)
                    .map(l -> mapToOrderDTO(l, i.getAndIncrement(), filePath.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    private OrderDTO mapToOrderDTO(String csv, Long lineNumber, String fileName) {
        OrderDTO orderDTO = new OrderDTO();

        CsvSchema schema = CsvSchema.builder()
                .addColumn("id")
                .addColumn("amount")
                .addColumn("currency")
                .addColumn("comment")
                .build();
        try {
            MappingIterator<OrderDTO> it = csvMapper.readerFor(OrderDTO.class).with(schema)
                    .readValues(csv);
            if (it.hasNextValue())
                orderDTO = it.nextValue();

        } catch (IOException e) {
            e.printStackTrace();
        }
        orderDTO.setFileName(fileName);
        orderDTO.setLine(lineNumber);
        return orderDTO;
    }
}
