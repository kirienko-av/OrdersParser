package ru.kirienko.ordersparser.parser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kirienko.ordersparser.annotation.FileType;
import ru.kirienko.ordersparser.domain.Order;
import ru.kirienko.ordersparser.parser.OrderParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Component
@FileType("json")
public class JsonOrderParser implements OrderParser {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Stream<Order> lines(Path filePath) {
        AtomicLong i = new AtomicLong(1);
        Stream<Order> stream = Stream.empty();

        try {
            stream = Files.lines(filePath)
                    .map(this::mapToOrderDTO)
                    .peek(o -> {
                        o.setFileName(filePath.getFileName().toString());
                        o.setLine(i.getAndIncrement());
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    private Order mapToOrderDTO(String json) {
        Order order = new Order();
        try {
            order = objectMapper
                    .addMixIn(Order.class, OrderJsonFormat.class)
                    .readValue(json, Order.class);
        } catch (IOException e) {
            order.setResult("ERROR: Invalid json");
        }
        return order;
    }
}
