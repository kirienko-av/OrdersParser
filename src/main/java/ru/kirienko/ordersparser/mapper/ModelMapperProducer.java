package ru.kirienko.ordersparser.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Optionals;
import ru.kirienko.ordersparser.domain.Order;
import ru.kirienko.ordersparser.domain.OrderLine;
import ru.kirienko.ordersparser.domain.OrderValidation;
import ru.kirienko.ordersparser.service.OrderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Configuration
public class ModelMapperProducer {

    private final CSVParser csvParser;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ModelMapperProducer(CSVParser csvParser, OrderService orderService, ObjectMapper objectMapper) {
        this.csvParser = csvParser;
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        modelMapper.typeMap(OrderLine.class, Order.class, "csv")
                .addMappings(mp -> mp.skip(Order::setLine))
                .setPostConverter(context -> {
                    try {
                        final List<OrderValidation> orderValidations = new ArrayList<>();
                        final String[] values = csvParser.parseLine(context.getSource().getLine());

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("id", values[0]))
                                .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setId(Long.parseLong(values[0])));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("amount", values[1]))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setAmount(new BigDecimal(values[1])));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("currency", values[2]))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setCurrency(values[2]));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("comment", values[3]))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setComment(values[3]));

                        context.getDestination().setLine(context.getSource().getLineNumber().longValue());
                        context.getDestination().setFileName(context.getSource().getFileName());


                        if(orderValidations.isEmpty())
                            context.getDestination().setResult("OK");
                        else
                            context.getDestination().setResult(objectMapper.writeValueAsString(orderValidations));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return context.getDestination();
                });

        modelMapper.typeMap(OrderLine.class, Order.class, "json")
                .addMappings(mp -> mp.skip(Order::setLine))
                .setPostConverter(context -> {
                    try {
                        final List<OrderValidation> orderValidations = new ArrayList<>();
                        final HashMap<String, String> values = objectMapper.readValue(context.getSource().getLine(), new TypeReference<HashMap<String, String>>(){});

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("id",  values.get("orderId")))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setId(Long.parseLong( values.get("orderId"))));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("amount", values.get("amount")))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setAmount(new BigDecimal( values.get("amount"))));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("currency", values.get("currency")))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setCurrency(values.get("currency")));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("comment", values.get("comment")))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setComment(values.get("comment")));

                        context.getDestination().setLine(context.getSource().getLineNumber().longValue());
                        context.getDestination().setFileName(context.getSource().getFileName());


                        if(orderValidations.isEmpty())
                            context.getDestination().setResult("OK");
                        else
                            context.getDestination().setResult(objectMapper.writeValueAsString(orderValidations));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return context.getDestination();
                });

        return modelMapper;
    }
}
