package ru.kirienko.ordersparser;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.kirienko.ordersparser.integration.IntegrationConfiguration;
import ru.kirienko.ordersparser.integration.OrderParserGateway;

import java.util.Arrays;

@ComponentScan
public class OrdersParserApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(OrdersParserApplication.class);
		OrderParserGateway orderGateway = ctx.getBean(OrderParserGateway.class);

		orderGateway.parse(Arrays.asList("D:\\install.log"));
	}

}
