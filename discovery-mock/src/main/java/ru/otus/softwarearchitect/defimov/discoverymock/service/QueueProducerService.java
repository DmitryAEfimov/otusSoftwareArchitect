package ru.otus.softwarearchitect.defimov.discoverymock.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.otus.softwarearchitect.defimov.discoverymock.service.dto.NotificationMessage;

@Component
public class QueueProducerService {
	private final RabbitTemplate rabbitTemplate;

	public QueueProducerService(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void fireInitializationComplete(NotificationMessage notificationMessage) {
		rabbitTemplate.convertAndSend(notificationMessage);
	}
}