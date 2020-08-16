package ru.otus.softwarearchitect.defimov.devicelibrary.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.dto.DiscoveryReportConverter;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.dto.DiscoveryReportDto;

@Component
public class QueueReceiverService {
	private final UnknownModelFinderService unknownModelFinder;
	private final DiscoveryReportConverter converter;

	public QueueReceiverService(UnknownModelFinderService unknownModelFinder, DiscoveryReportConverter converter) {
		this.unknownModelFinder = unknownModelFinder;
		this.converter = converter;
	}

	public void receiveMessage(byte[] content) {
		Message message = MessageBuilder.withBody(content).build();
		unknownModelFinder.updateCache((DiscoveryReportDto) converter.fromMessage(message));
	}
}
