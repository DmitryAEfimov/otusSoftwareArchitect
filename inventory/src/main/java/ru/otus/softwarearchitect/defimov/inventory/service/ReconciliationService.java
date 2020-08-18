package ru.otus.softwarearchitect.defimov.inventory.service;

import com.google.common.collect.Streams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkElement;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkElementRepository;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkStatus;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.ReconciliationRepository;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationTask;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.TaskStatus;
import ru.otus.softwarearchitect.defimov.inventory.service.dto.ReportItemChunkDto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class ReconciliationService {
	private static final String EXECUTION_ERROR_SUMMARY = "Execution error: ";

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
		ReconciliationTask task = reconsiliationRepository.initTask();

		try {
			CompletableFuture.runAsync(() -> doTask(task, discoveryReportId, chunkSize != null ? chunkSize : 100));
		} catch (Exception ex) {
			task.setTaskStatus(TaskStatus.Error);
			task.setStatusDetailInfo(EXECUTION_ERROR_SUMMARY + ex.getMessage());
		}

		reconsiliationRepository.finishTask(task);
	}

	private void doTask(ReconciliationTask task, UUID discoveryReportId, int chunkSize) {
		reconsiliationRepository.startTask(task);

		Set<NetworkElement> processedNe = processDiscovered(task.getId(), discoveryReportId, chunkSize);
		processOrphan(task.getId(), processedNe);

		task.setTaskStatus(TaskStatus.Finished);
	}

	public Set<NetworkElement> processDiscovered(UUID taskId, UUID discoveryReportId, int chunkSize) {
		int pageNum = 0;
		Set<NetworkElement> processedNe = new HashSet<>();
		while (true) {
			String completeUrl = String
					.format("http://%s:%d/data/%s?page=%d&size=%d", discoveryServiceHost,
							discoveryServicePort, discoveryReportId, pageNum++, chunkSize);
			ReportItemChunkDto chunk = restTemplate.getForObject(completeUrl, ReportItemChunkDto.class);
			if (chunk != null) {
				chunk.getContent().stream().map(item -> enrichNetworkElement(taskId, item)).filter(Objects::nonNull)
						.forEach(processedNe::add);

				if (chunk.isLast())
					break;
			}
		}
		neRepository.saveAll(processedNe);

		return processedNe;
	}

	public void processOrphan(UUID taskId, Set<NetworkElement> processedNe) {
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
		reconsiliationRepository.saveNetworkUnavailable(taskId, orphanNe);
	}

	private NetworkElement enrichNetworkElement(UUID taskId, ReportItemChunkDto.Item item) {
		NetworkElement[] ne = new NetworkElement[1];
		neRepository.findBySnmpAgentNameAndIp(item.getSnmpAgentName(), item.getIp())
				.ifPresentOrElse(fromDb -> ne[0] = fromDb,
						() -> {
							reconsiliationRepository
									.saveNotFoundInInventory(taskId, item.getSnmpAgentName(), item.getIp());
						});
		libraryService.findDeviceDescriptor(item.getDeviceModel())
				.ifPresentOrElse(
						descriptor -> {
							if (ne[0] != null) {
								ne[0].setDeviceDescriptor(descriptor);
							}
						},
						() -> {
							ne[0].setDeviceDescriptor(null);
							reconsiliationRepository.saveUnknownModel(taskId, item.getDeviceModel());
						});

		if (ne[0] != null) {
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