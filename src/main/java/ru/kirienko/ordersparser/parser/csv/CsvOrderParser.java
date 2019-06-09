package ru.kirienko.ordersparser.parser.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kirienko.ordersparser.annotation.FileType;
import ru.kirienko.ordersparser.domain.Order;
import ru.kirienko.ordersparser.parser.OrderParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
@FileType("csv")
public class CsvOrderParser implements OrderParser {

    @Autowired
    private CsvMapper csvMapper;

    @Override
    public Stream<Order> lines(Path filePath) {
        AtomicLong i = new AtomicLong(1);
        Stream<Order> stream = Stream.empty();

        try {
            MappingIterator<Order> iterator = csvMapper
                    .addMixIn(Order.class, OrderCsvFormat.class)
                    .readerFor(Order.class)
                    .with(csvMapper.schemaFor(OrderCsvFormat.class)
                            .withSkipFirstDataRow(true))
                    .readValues(filePath.toFile());

            stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator,
                    Spliterator.ORDERED),
                    false)
                    .peek(o -> {
                        o.setFileName(filePath.getFileName().toString());
                        o.setLine(i.getAndIncrement());
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
