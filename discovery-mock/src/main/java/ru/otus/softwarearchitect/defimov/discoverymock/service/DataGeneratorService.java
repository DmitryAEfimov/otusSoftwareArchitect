package ru.otus.softwarearchitect.defimov.discoverymock.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import ru.otus.softwarearchitect.defimov.discoverymock.model.DiscoveryReportItem;
import ru.otus.softwarearchitect.defimov.discoverymock.model.ReportRepository;
import ru.otus.softwarearchitect.defimov.discoverymock.service.dto.DiscoveryReportDto;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DataGeneratorService {
	private final RestTemplate restTemplate;
	private final ReportRepository reportRepository;
	private final MessageSource messageSource;

	@Value("${app.stubgenerator.host:}")
	private String host;
	@Value("${app.stubgenerator.port:8080}")
	private int port;

	public DataGeneratorService(RestTemplate restTemplate, ReportRepository reportRepository,
			MessageSource messageSource) {
		this.restTemplate = restTemplate;
		this.reportRepository = reportRepository;
		this.messageSource = messageSource;
	}

	public UUID generate(int dataCnt) {
		String completeUrl = String
				.format("http://%s:%d/faker/data?items_cnt=%d", host, port, dataCnt);
		DiscoveryReportDto dtoObject = restTemplate.getForObject(completeUrl, DiscoveryReportDto.class);
		UUID reportId = UUID.randomUUID();

		Assert.notNull(dtoObject,
				messageSource.getMessage("noDataWereGenerated", new Object[] { completeUrl }, Locale.US));
		Set<DiscoveryReportItem> reportItems = dtoObject.getItems().stream().map(item -> toModel(reportId, item))
				.collect(Collectors.toSet());
		reportRepository.saveAll(reportItems);

		return reportId;
	}

	private DiscoveryReportItem toModel(UUID reportId, DiscoveryReportDto.ItemDTO dto) {
		return new DiscoveryReportItem(reportId, dto.getNetworkDomen(), dto.getIpAddress(), dto.getModel(),
				dto.getElementStatus());
	}
}
