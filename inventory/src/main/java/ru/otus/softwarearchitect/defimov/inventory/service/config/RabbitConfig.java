package ru.otus.softwarearchitect.defimov.inventory.service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.softwarearchitect.defimov.inventory.service.QueueReceiverService;

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
	public FanoutExchange networkDiscoveryExchange(@Value("${app.rabbit.discovery.exchange}") String exchangeName) {
		return ExchangeBuilder.fanoutExchange(exchangeName).build();
	}

	@Bean
	public Queue networkDiscoveryQueue(@Value("${app.rabbit.discovery.queue}") String queueName) {
		return QueueBuilder.durable(queueName).lazy().autoDelete().build();
	}

	@Bean
	public Binding networkDiscoveryBinding(FanoutExchange networkDiscoveryExchange, Queue networkDiscoveryQueue) {
		return BindingBuilder.bind(networkDiscoveryQueue).to(networkDiscoveryExchange);
	}

	@Bean
	MessageListenerAdapter listenerAdapter(QueueReceiverService receiverService,
			@Value("${app.rabbit.discovery.queue}") String queueName) {
		MessageListenerAdapter messageAdapter = new MessageListenerAdapter(receiverService);
		messageAdapter.addQueueOrTagToMethodName(queueName, "receiveMessage");

		return messageAdapter;
	}

	@Bean
	public SimpleMessageListenerContainer simpleMessageListenerContainer(MessageListenerAdapter listenerAdapter,
			Queue networkDiscoveryQueue,
			ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(networkDiscoveryQueue);
		container.setMessageListener(listenerAdapter);

		return container;
	}
}

