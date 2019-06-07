package ru.kirienko.ordersparser.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.kirienko.ordersparser.service.ParserService;

@Component
public class OrdersParserRunner implements ApplicationRunner {

    @Autowired
    private ParserService parserService;

    @Override
    public void run(ApplicationArguments args) {
        args.getNonOptionArgs().stream()
                .flatMap(parserService::getOrderStream)
                .parallel()
                .forEach(System.out::println);
    }
}
