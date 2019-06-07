package ru.kirienko.ordersparser.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kirienko.ordersparser.dto.OrderDTO;
import ru.kirienko.ordersparser.parser.OrderParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Component
public class JsonOrderParser implements OrderParser {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Stream<OrderDTO> getOrderStream(String filePath) {
        Path path = Paths.get(filePath);
        AtomicLong i = new AtomicLong(0);
        Stream<OrderDTO> stream = Stream.empty();
        try {
            stream = Files.lines(path)
                    .map(l -> mapToOrderDTO(l, i.getAndIncrement(), path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    private OrderDTO mapToOrderDTO(String json, Long lineNumber, String fileName) {
        OrderDTO orderDTO = new OrderDTO();
        try {
            orderDTO = objectMapper
                    .addMixIn(OrderDTO.class, OrderMixIn.class)
                    .readValue(json, OrderDTO.class);
        } catch (IOException e) {
            orderDTO.setResult("Invalid line");
        } finally {
            orderDTO.setFileName(fileName);
            orderDTO.setLine(lineNumber);
        }
        return orderDTO;
    }
}
