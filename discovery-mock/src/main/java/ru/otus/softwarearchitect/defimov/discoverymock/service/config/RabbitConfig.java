package ru.otus.softwarearchitect.defimov.discoverymock.service.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	@Value("${app.rabbit.exchange}")
	private String exchangeName;

	@Bean
	@Qualifier(value = "networkDiscoveryExchange")
	public Exchange networkDiscoveryExchange() {
		return ExchangeBuilder.fanoutExchange(exchangeName).build();
	}
}
