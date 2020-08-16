package ru.otus.softwarearchitect.defimov.discoverymock.service.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.softwarearchitect.defimov.discoverymock.service.dto.DiscoveryReportConverter;

@Configuration
@EnableRabbit
public class RabbitConfig {
	@Bean
	public ConnectionFactory connectionFactory(
			@Value("${spring.rabbitmq.host}") String hostname,
			@Value("${spring.rabbitmq.port}") int port,
			@Value("${spring.rabbitmq.username}") String username,
			@Value("${spring.rabbitmq.password}") String password) {
		CachingConnectionFactory factory = new CachingConnectionFactory(hostname, port);
		factory.setUsername(username);
		factory.setPassword(password);

		return factory;
	}

	@Bean
	public Exchange networkDiscoveryExchange(@Value("${app.rabbit.exchange}") String exchangeName) {
		return ExchangeBuilder.fanoutExchange(exchangeName).build();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
			@Value("${app.rabbit.exchange}") String exchangeName, DiscoveryReportConverter reportConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange(exchangeName);
		rabbitTemplate.setMessageConverter(reportConverter);

		return rabbitTemplate;
	}
}
