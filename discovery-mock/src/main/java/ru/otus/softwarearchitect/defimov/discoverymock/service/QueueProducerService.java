package ru.otus.softwarearchitect.defimov.discoverymock.service;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.softwarearchitect.defimov.discoverymock.service.dto.NotificationMessage;

@Component
public class QueueProducerService {
	private RabbitTemplate rabbitTemplate;

	private Exchange networkDiscoveryExchange;

	public QueueProducerService(RabbitTemplate rabbitTemplate,
			@Qualifier(value = "networkDiscoveryExchange") Exchange networkDiscoveryExchange) {
		this.rabbitTemplate = rabbitTemplate;
		this.networkDiscoveryExchange = networkDiscoveryExchange;
	}

	public void fireInitializationComplete(NotificationMessage message) {
		rabbitTemplate.convertAndSend(networkDiscoveryExchange.getName(), message);
	}
}