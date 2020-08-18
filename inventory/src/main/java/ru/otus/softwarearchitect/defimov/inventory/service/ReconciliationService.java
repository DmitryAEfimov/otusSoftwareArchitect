package ru.otus.softwarearchitect.defimov.inventory.service;

import com.google.common.collect.Streams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.DeviceDescriptor;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkElement;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkElementRepository;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkStatus;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.ReconciliationRepository;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationTask;
import ru.otus.softwarearchitect.defimov.inventory.service.dto.ReportItemChunkDto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class ReconciliationService {
	@Value(value = "${app.ext-service.discovery.host}")
	private String discoveryServiceHost;

	@Value(value = "${app.ext-service.discovery.port}")
	private int discoveryServicePort;

	private final RestTemplate restTemplate;
	private final ReconciliationRepository reconsiliationRepository;
	private final NetworkElementRepository neRepository;
	private DeviceLibraryService libraryService;

	public ReconciliationService(RestTemplate restTemplate, ReconciliationRepository reconciliationRepository,
			NetworkElementRepository neRepository, DeviceLibraryService libraryService) {
		this.restTemplate = restTemplate;
		this.reconsiliationRepository = reconciliationRepository;
		this.neRepository = neRepository;
		this.libraryService = libraryService;
	}

	public void start(UUID discoveryReportId, @Nullable Integer chunkSize) {
		ReconciliationTask task = reconsiliationRepository.initTask(discoveryReportId);

		try {
			CompletableFuture.runAsync(() -> doTask(task, chunkSize != null ? chunkSize : 100));
		} catch (Exception ex) {
			reconsiliationRepository.finishTask(task, ex);
		}
	}

	private void doTask(ReconciliationTask task, int chunkSize) {
		reconsiliationRepository.startTask(task);

		Set<NetworkElement> processedNe = processDiscovered(task, chunkSize);
		processOrphan(task, processedNe);

		reconsiliationRepository.finishTask(task);
	}

	public Set<NetworkElement> processDiscovered(ReconciliationTask task, int chunkSize) {
		int pageNum = 0;
		Set<NetworkElement> processedNe = new HashSet<>();
		while (true) {
			String completeUrl = String
					.format("http://%s:%d/discovery/data/%s?page=%d&size=%d", discoveryServiceHost,
							discoveryServicePort, task.getDiscoveryReportId(), pageNum++, chunkSize);
			ReportItemChunkDto chunk = restTemplate.getForObject(completeUrl, ReportItemChunkDto.class);
			if (chunk != null) {
				chunk.getContent().stream().map(item -> enrichNetworkElement(task.getId(), item))
						.filter(Objects::nonNull)
						.forEach(processedNe::add);

				if (chunk.isLast())
					break;
			}
		}
		neRepository.saveAll(processedNe);

		return processedNe;
	}

	public void processOrphan(ReconciliationTask task, Set<NetworkElement> processedNe) {
		Set<NetworkElement> orphanNe = Streams.stream(neRepository.findAll()).filter(ne -> !processedNe.contains(ne))
				.peek(ne -> {
					NetworkStatus currentStatus = ne.getNetworkStatus();
					if (Set.of(NetworkStatus.PreUnavailable, NetworkStatus.Unavailable).contains(currentStatus)) {
						ne.setDeviceDescriptor(null);
						ne.setNetworkStatus(NetworkStatus.Unavailable);
					} else if (currentStatus != NetworkStatus.Undefined) {
						// place in quarantine unitl next discovery
						ne.setNetworkStatus(NetworkStatus.PreUnavailable);
					}
				}).collect(Collectors.toSet());
		neRepository.saveAll(orphanNe);
		reconsiliationRepository.saveNetworkUnavailable(task.getId(), orphanNe);
	}

	private NetworkElement enrichNetworkElement(UUID taskId, ReportItemChunkDto.Item item) {
		NetworkElement[] ne = new NetworkElement[1];
		neRepository.findBySnmpAgentNameAndIp(item.getSnmpAgentName(), item.getIp())
				.ifPresentOrElse(fromDb -> ne[0] = fromDb,
						() -> {
							reconsiliationRepository
									.saveNotFoundInInventory(taskId, item.getSnmpAgentName(), item.getIp());
						});

		Optional<DeviceDescriptor> deviceDescriptor = libraryService.findDeviceDescriptor(item.getDeviceModel());

		if (deviceDescriptor.isEmpty()) {
			reconsiliationRepository.saveUnknownModel(taskId, item.getDeviceModel());
		}

		if (ne[0] != null) {
			ne[0].setDeviceDescriptor(deviceDescriptor.orElse(null));

			NetworkStatus newStatus = item.getElementStatus();
			switch (ne[0].getNetworkStatus()) {
			case Undefined:
			case Active:
			case OnHold:
				// place in quarantine unitl next discovery
				ne[0].setNetworkStatus(
						newStatus != NetworkStatus.Unavailable ? newStatus : NetworkStatus.PreUnavailable);
				break;
			case PreUnavailable:
			case Unavailable:
				ne[0].setNetworkStatus(newStatus);
			}
		}

		return ne[0];
	}
}