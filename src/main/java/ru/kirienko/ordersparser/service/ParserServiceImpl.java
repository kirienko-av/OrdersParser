package ru.kirienko.ordersparser.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kirienko.ordersparser.dto.OrderDTO;
import ru.kirienko.ordersparser.parser.json.JsonOrderParser;

import java.util.stream.Stream;

@Service
public class ParserServiceImpl implements ParserService {

    @Autowired
    private JsonOrderParser jsonOrderParser;

    @Override
    public Stream<OrderDTO> getOrderStream(String filePath) {
        return jsonOrderParser.getOrderStream(filePath);
    }
}
