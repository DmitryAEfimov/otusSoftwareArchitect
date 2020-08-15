package ru.otus.softwarearchitect.defimov.discoverymock.service.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
	@Value("${app.rabbit.exchange}")
	private String exchangeName;

	@Value("${spring.rabbitmq.host:}")
	private String hostname;

	@Value("${spring.rabbitmq.port:5672}")
	private int port;

	@Value("${spring.rabbitmq.username:}")
	private String username;

	@Value("${spring.rabbitmq.password:}")
	private String password;

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory(hostname, port);
		factory.setUsername(username);
		factory.setPassword(password);

		return factory;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setMaxConcurrentConsumers(5);
		return factory;
	}

	@Bean
	public Exchange networkDiscoveryExchange() {
		return ExchangeBuilder.fanoutExchange(exchangeName).build();
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setExchange(networkDiscoveryExchange().getName());

		return rabbitTemplate;
	}
}
