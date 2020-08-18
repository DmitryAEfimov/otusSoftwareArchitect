package ru.otus.softwarearchitect.defimov.inventory.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.otus.softwarearchitect.defimov.inventory.service.dto.DiscoveryReportConverter;
import ru.otus.softwarearchitect.defimov.inventory.service.dto.DiscoveryReportDto;

@Component
public class QueueReceiverService {
	private final ReconciliationService reconciliationService;
	private final DiscoveryReportConverter converter;

	public QueueReceiverService(ReconciliationService reconciliationService, DiscoveryReportConverter converter) {
		this.reconciliationService = reconciliationService;
		this.converter = converter;
	}

	public void receiveMessage(byte[] content) {
		Message message = MessageBuilder.withBody(content).build();

		DiscoveryReportDto discoveryReport = (DiscoveryReportDto) converter.fromMessage(message);
		reconciliationService.start(discoveryReport.getId(), discoveryReport.getElementsCount());
	}
}
