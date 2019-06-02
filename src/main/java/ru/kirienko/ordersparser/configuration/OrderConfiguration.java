package ru.kirienko.ordersparser.configuration;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.kirienko.ordersparser.integration.OrderItemReader;

@Configuration
public class OrderConfiguration {
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public OrderItemReader orderItemReader(Class cls) throws IllegalAccessException, InstantiationException {
        return (OrderItemReader) cls.newInstance();
    }
}
