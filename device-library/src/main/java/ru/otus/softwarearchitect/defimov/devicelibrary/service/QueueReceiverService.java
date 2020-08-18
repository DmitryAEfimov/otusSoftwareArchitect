package ru.otus.softwarearchitect.defimov.devicelibrary.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.dto.DiscoveryReportConverter;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.dto.DiscoveryReportDto;

@Component
public class QueueReceiverService {
	private final RabbitTemplate rabbitTemplate;
	private final CacheService cacheService;
	private final DiscoveryReportConverter converter;

	public QueueReceiverService(RabbitTemplate rabbitTemplate, CacheService cacheService,
			DiscoveryReportConverter converter) {
		this.rabbitTemplate = rabbitTemplate;
		this.cacheService = cacheService;
		this.converter = converter;
	}

	public void receiveDiscoveryMessage(byte[] content) {
		rabbitTemplate.send(MessageBuilder.withBody(content).build());
	}

	public void receiveSyncMessage(byte[] content) {
		Message message = MessageBuilder.withBody(content).build();
		cacheService.updateCache((DiscoveryReportDto) converter.fromMessage(message));
	}
}
