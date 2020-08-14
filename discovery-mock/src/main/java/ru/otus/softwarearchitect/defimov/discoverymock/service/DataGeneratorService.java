package ru.otus.softwarearchitect.defimov.discoverymock.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import ru.otus.softwarearchitect.defimov.discoverymock.model.DiscoveryReportItem;
import ru.otus.softwarearchitect.defimov.discoverymock.model.ReportRepository;
import ru.otus.softwarearchitect.defimov.discoverymock.model.dto.DiscoveryReportDTO;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DataGeneratorService {
	private final RestTemplate restTemplate;
	private final ReportRepository reportRepository;
	private final MessageSource messageSource;

	@Value("${app.stubgenerator.host}")
	private String host;
	@Value("${app.stubgenerator.path}")
	private String path;
	@Value("${app.stubgenerator.port}")
	private int port;

	public DataGeneratorService(RestTemplate restTemplate, ReportRepository reportRepository,
			MessageSource messageSource) {
		this.restTemplate = restTemplate;
		this.reportRepository = reportRepository;
		this.messageSource = messageSource;
	}

	public void generate(int dataCnt) {
		String completeUrl = String
				.format("http://%s:%d/%s?items_cnt=%d", host, port, path, dataCnt);
		DiscoveryReportDTO dtoObject = restTemplate.getForObject(completeUrl, DiscoveryReportDTO.class);
		UUID reportId = UUID.randomUUID();

		Assert.notNull(dtoObject,
				messageSource.getMessage("noDataWereGenerated", new Object[] { completeUrl }, Locale.US));
		Set<DiscoveryReportItem> reportItems = dtoObject.getItems().stream().map(item -> toModel(reportId, item))
				.collect(Collectors.toSet());
		reportRepository.saveAll(reportItems);
	}

	private DiscoveryReportItem toModel(UUID reportId, DiscoveryReportDTO.ItemDTO dto) {
		return new DiscoveryReportItem(reportId, dto.networkDomen, dto.ipAddress, dto.model, dto.elementStatus);
	}
}
