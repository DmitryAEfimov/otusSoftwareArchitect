package ru.otus.softwarearchitect.defimov.devicelibrary.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.DeviceModel;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.UnknownDeviceRepository;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.dto.DiscoveryReportDto;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.dto.ReportItemChunkDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UnknownModelFinderService {

	@Value(value = "${app.discoveryservice.host}")
	private String discoveryServiceHost;

	@Value(value = "${app.discoveryservice.port}")
	private int discoveryServicePort;

	private final RestTemplate restTemplate;
	private final UnknownDeviceRepository deviceRepository;
	private final MessageSource messageSource;

	private DiscoveryReportDto lastDiscoveryReport;
	private Map<UUID, Set<DeviceModel>> unknownModelsCache;

	public UnknownModelFinderService(RestTemplate restTemplate, UnknownDeviceRepository unknownDeviceRepository,
			MessageSource messageSource) {
		this.restTemplate = restTemplate;
		this.deviceRepository = unknownDeviceRepository;
		this.messageSource = messageSource;
	}

	public void updateCache(DiscoveryReportDto report) {
		clearCache();
		doUpdateCache(report);
	}

	public void updateCache() {
		updateCache(lastDiscoveryReport);
	}

	public Set<DeviceModel> findUnknownModels() {
		if (unknownModelsCache == null && lastDiscoveryReport != null) {
			doUpdateCache(lastDiscoveryReport);
		}

		return lastDiscoveryReport != null ? unknownModelsCache.get(lastDiscoveryReport.getId()) :
				Collections.emptySet();
	}

	public void clearCache() {
		unknownModelsCache = null;
	}

	private void doUpdateCache(DiscoveryReportDto report) {
		Set<String> discoveredModels = getDiscoveredModels(report.getId());

		Map<String, Set<String>> unknownModels = deviceRepository.findUnknownModels(discoveredModels);

		unknownModelsCache = new HashMap<>(unknownModels.values().stream().mapToInt(Set::size).sum());
		unknownModels.forEach((vendor, models) -> models.forEach(model -> {
			unknownModelsCache.putIfAbsent(report.getId(), new HashSet<>());
			unknownModelsCache.get(report.getId()).add(new DeviceModel(vendor, model));
		}));

		this.lastDiscoveryReport = report;
	}

	private Set<String> getDiscoveredModels(UUID reportId) {
		int pageNum = 0;
		int chunkSize = 100;

		Set<String> models = new HashSet<>();
		while (true) {
			String completeUrl = String
					//					.format("http://%s:%d/discovery/data/%s?page=%d&size=%d", discoveryServiceHost,
					.format("http://%s:%d/data/%s?page=%d&size=%d", discoveryServiceHost,
							discoveryServicePort, reportId, pageNum++, chunkSize);
			ReportItemChunkDto chunk = restTemplate.getForObject(completeUrl, ReportItemChunkDto.class);
			Assert.notNull(chunk, messageSource.getMessage("noDataFound", new Object[] { completeUrl }, Locale.US));
			models.addAll(extractModels(chunk));

			if (chunk.isLast())
				break;
		}

		return models;
	}

	private Set<String> extractModels(ReportItemChunkDto chunk) {
		return chunk.getContent().stream().map(ReportItemChunkDto.DeviceModelDto::getDeviceModel)
				.collect(Collectors.toSet());
	}

}
