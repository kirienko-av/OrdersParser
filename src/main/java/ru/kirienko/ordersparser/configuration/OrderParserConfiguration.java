package ru.kirienko.ordersparser.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderParserConfiguration {
    @Bean
    public CSVParser csvParser(){
        return new CSVParser();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
