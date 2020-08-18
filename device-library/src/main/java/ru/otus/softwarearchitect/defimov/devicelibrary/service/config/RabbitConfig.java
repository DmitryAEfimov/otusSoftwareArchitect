package ru.otus.softwarearchitect.defimov.devicelibrary.service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.QueueReceiverService;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.dto.DiscoveryReportConverter;

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
		return QueueBuilder.durable(queueName).lazy().build();
	}

	@Bean
	public FanoutExchange librarySyncExchange(
			@Value(value = "${app.rabbit.library.sync.exchange}") String exchangeName) {
		return ExchangeBuilder.fanoutExchange(exchangeName).build();
	}

	@Bean
	public Queue librarySyncQueue(@Value("${app.rabbit.library.sync.queue}") String queueName) {
		return QueueBuilder.durable(queueName).lazy().build();
	}

	@Bean
	public Binding networkDiscoveryBinding(FanoutExchange networkDiscoveryExchange, Queue networkDiscoveryQueue) {
		return BindingBuilder.bind(networkDiscoveryQueue).to(networkDiscoveryExchange);
	}

	@Bean
	public Binding librarySyncBinding(FanoutExchange librarySyncExchange, Queue librarySyncQueue) {
		return BindingBuilder.bind(librarySyncQueue).to(librarySyncExchange);
	}

	@Bean
	MessageListenerAdapter listenerAdapter(QueueReceiverService receiverService,
			@Value("${app.rabbit.discovery.queue}") String queueName) {
		MessageListenerAdapter messageAdapter = new MessageListenerAdapter(receiverService);
		messageAdapter.addQueueOrTagToMethodName(queueName, "receiveDiscoveryMessage");
		messageAdapter.addQueueOrTagToMethodName(queueName, "receiveSyncMessage");

		return messageAdapter;
	}

	@Bean
	public SimpleMessageListenerContainer simpleMessageListenerContainer(MessageListenerAdapter listenerAdapter,
			Queue networkDiscoveryQueue, Queue librarySyncQueue,
			ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(networkDiscoveryQueue);
		container.setQueues(librarySyncQueue);
		container.setMessageListener(listenerAdapter);

		return container;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
			@Value("${app.rabbit.library.sync.exchange}") String exchangeName,
			DiscoveryReportConverter reportConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange(exchangeName);
		rabbitTemplate.setMessageConverter(reportConverter);

		return rabbitTemplate;
	}
}
