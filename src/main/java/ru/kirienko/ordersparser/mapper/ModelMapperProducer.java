package ru.kirienko.ordersparser.mapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

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
                    final List<OrderValidation> orderValidations = new ArrayList<>();
                    try {
                        final String[] values = csvParser.parseLine(context.getSource().getLine());

                        if(values.length != 4)
                            throw new DataFormatException();

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("id", values[0]))
                                .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setId(Long.parseLong(values[0])));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("amount", values[1]))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setAmount(new BigDecimal(values[1])));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("currency", values[2].trim()))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setCurrency(values[2].trim()));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("comment", values[3].trim()))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setComment(values[3].trim()));

                        context.getDestination().setLine(context.getSource().getLineNumber().longValue());
                        context.getDestination().setFileName(context.getSource().getFileName());
                    } catch (IOException | DataFormatException e) {
                        OrderValidation orderValidation = new OrderValidation("orderData", context.getSource().getLine());
                        orderValidation.setDescription("Данные не соответствует формату csv");
                        orderValidations.add(orderValidation);
                    } finally {
                        context.getDestination().setLine(context.getSource().getLineNumber().longValue());
                        context.getDestination().setFileName(context.getSource().getFileName());
                        if(orderValidations.isEmpty())
                            context.getDestination().setResult("OK");
                        else {
                            try {
                                context.getDestination().setResult(objectMapper.writeValueAsString(orderValidations));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return context.getDestination();
                });

        modelMapper.typeMap(OrderLine.class, Order.class, "json")
                .addMappings(mp -> mp.skip(Order::setLine))
                .setPostConverter(context -> {
                    final List<OrderValidation> orderValidations = new ArrayList<>();
                    try {
                        final HashMap<String, String> values = objectMapper.readValue(context.getSource().getLine(), new TypeReference<HashMap<String, String>>() {
                        });

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("id", values.get("orderId")))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setId(Long.parseLong(values.get("orderId"))));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("amount", values.get("amount")))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setAmount(new BigDecimal(values.get("amount"))));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("currency", values.get("currency").trim()))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setCurrency(values.get("currency").trim()));

                        Optionals.ifPresentOrElse(Optional.ofNullable(orderService.validation("comment", values.get("comment").trim()))
                                        .filter(v -> !v.getDescription().equals("OK")),
                                orderValidations::add,
                                () -> context.getDestination().setComment(values.get("comment").trim()));

                    } catch (JsonMappingException |JsonParseException e) {
                        OrderValidation orderValidation = new OrderValidation("orderData", context.getSource().getLine());
                        orderValidation.setDescription("Данные не соответствует формату json");
                        orderValidations.add(orderValidation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        context.getDestination().setLine(context.getSource().getLineNumber().longValue());
                        context.getDestination().setFileName(context.getSource().getFileName());
                        if (orderValidations.isEmpty())
                            context.getDestination().setResult("OK");
                        else {
                            try {
                                context.getDestination().setResult(objectMapper.writeValueAsString(orderValidations));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return context.getDestination();
                });

        return modelMapper;
    }
}
