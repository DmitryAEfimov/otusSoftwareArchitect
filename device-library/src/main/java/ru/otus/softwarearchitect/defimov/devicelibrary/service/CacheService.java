package ru.otus.softwarearchitect.defimov.devicelibrary.service;

import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.SpecificationIdentity;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.SpecificationRepository;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.dto.DiscoveryReportDto;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.dto.ReportItemChunkDto;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.exception.DiscoveryReportNotExistsExcception;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CacheService {
	private Set<SpecificationIdentity> unknownModelsCache;
	private DiscoveryReportDto lastDiscoveryReport;

	@Value(value = "${app.discoveryservice.host}")
	private String discoveryServiceHost;

	@Value(value = "${app.discoveryservice.port}")
	private int discoveryServicePort;

	private final RestTemplate restTemplate;
	private final SpecificationRepository deviceRepository;

	public CacheService(RestTemplate restTemplate, SpecificationRepository unknownDeviceRepository) {
		this.restTemplate = restTemplate;
		this.deviceRepository = unknownDeviceRepository;
	}

	public void updateCache() throws DiscoveryReportNotExistsExcception {
		if (lastDiscoveryReport == null) {
			throw new DiscoveryReportNotExistsExcception();
		}

		updateCache(lastDiscoveryReport);
	}

	public synchronized void updateCache(DiscoveryReportDto report) {
		lastDiscoveryReport = report;

		Set<String> discoveredModels = getDiscoveredModels(lastDiscoveryReport.getId());
		unknownModelsCache = deviceRepository.findUnknownModels(discoveredModels);
	}

	public Set<SpecificationIdentity> getUnknownModels() {
		return ImmutableSet.copyOf(unknownModelsCache);
	}

	private Set<String> getDiscoveredModels(UUID reportId) {
		int pageNum = 0;
		int chunkSize = 100;

		Set<String> models = new HashSet<>();
		while (true) {
			String completeUrl = String
					.format("http://%s:%d/data/%s?page=%d&size=%d", discoveryServiceHost,
							discoveryServicePort, reportId, pageNum++, chunkSize);
			ReportItemChunkDto chunk = restTemplate.getForObject(completeUrl, ReportItemChunkDto.class);
			if (chunk != null) {
				models.addAll(extractModels(chunk));
				if (chunk.isLast())
					break;
			}
		}

		return models;
	}

	private Set<String> extractModels(ReportItemChunkDto chunk) {
		return chunk.getContent().stream().map(ReportItemChunkDto.DeviceModelDto::getDeviceModel)
				.collect(Collectors.toSet());
	}
}
