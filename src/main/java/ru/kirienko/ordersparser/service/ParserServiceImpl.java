package ru.kirienko.ordersparser.service;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.kirienko.ordersparser.annotation.FileType;
import ru.kirienko.ordersparser.domain.Order;
import ru.kirienko.ordersparser.parser.OrderParser;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ParserServiceImpl implements ParserService {

    @Autowired
    private ApplicationContext context;


    @Override
    public Stream<Order> lines(String filePath) {
        final Stream<Order> stream;
        ClassInfoList classInfos = new ClassGraph().enableAllInfo().scan()
                .getClassesWithAnnotation(FileType.class.getName())
                .filter(classInfo -> classInfo.implementsInterface(OrderParser.class.getName()))
                .filter(classInfo -> classInfo.getAnnotationInfo(FileType.class.getName())
                        .getParameterValues().getValue("value")
                        .equals(FilenameUtils.getExtension(filePath))
                );

        if (classInfos.size() == 1) {
            OrderParser orderParser = context.getBean(classInfos.get(0)
                    .loadClass(OrderParser.class));

            stream = orderParser.lines(Paths.get(filePath));
        } else {
            stream = Stream.empty();
            System.out.println("Reading class not found for extension " + FilenameUtils.getExtension(filePath));
        }

        return stream;
    }

    @Override
    public Order validate(Order order) {
        List<String> errors = new ArrayList<>(4);

        if (order.getResult() != null)
            return order;

        valivade("id", order.getId(), "^\\d+$")
                .ifPresent(errors::add);
        valivade("amount", order.getAmount(), "^\\d+$")
                .ifPresent(errors::add);
        valivade("currency", order.getCurrency(), "^[A-Z]+$")
                .ifPresent(errors::add);
        valivade("comment", order.getComment(), "^\\S+(.*\\S+)*$")
                .ifPresent(errors::add);

        if (errors.isEmpty())
            order.setResult("OK");
        else
            order.setResult("ERROR: " + errors.stream()
                    .collect(Collectors.joining("; ", "{", "}")));
        return order;
    }

    private Optional<String> valivade(String field, String value, String regex) {
        Optional<String> optionalValue = Optional.ofNullable(value);
        if (!optionalValue.isPresent())
            return Optional.of("The value of the " + field + " field cannot be null");
        if (!optionalValue.filter(v -> v.matches(regex)).isPresent())
            return Optional.of("The value of the " + field + " field must match the mask " + regex);
        return Optional.empty();
    }
}
