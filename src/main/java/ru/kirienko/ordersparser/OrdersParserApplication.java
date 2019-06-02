package ru.kirienko.ordersparser;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.kirienko.ordersparser.integration.IntegrationConfiguration;
import ru.kirienko.ordersparser.integration.OrderParserGateway;

import java.util.Arrays;

public class OrdersParserApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(IntegrationConfiguration.class);
		OrderParserGateway orderGateway = ctx.getBean(OrderParserGateway.class);

		orderGateway.parse(Arrays.asList("D:\\install.log"));
	}

}
