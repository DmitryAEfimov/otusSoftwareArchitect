package ru.otus.softwarearchitect.defimov.inventory.model.reconciliation;

import org.springframework.stereotype.Repository;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.DeviceDescriptor;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkElement;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkStatus;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationError;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationErrorType;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationTask;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.TaskStatus;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class ReconciliationRepositoryImpl implements ReconciliationRepository {
	private ReconciliationTaskRepository taskRepository;
	private ReconciliationErrorRepository errorRepository;

	public ReconciliationRepositoryImpl(
			ReconciliationTaskRepository taskRepository, ReconciliationErrorRepository errorRepository) {
		this.taskRepository = taskRepository;
		this.errorRepository = errorRepository;
	}

	@Override
	public void saveNotFoundInInventory(UUID taskId, String snmpAgentName, String ipAddress) {
		errorRepository.save(new ReconciliationError(taskId, ReconciliationErrorType.NO_NE_FOUND,
				buildNeIdentity(snmpAgentName, ipAddress)));
	}

	@Override
	public void saveUnknownModel(UUID taskId, String deviceModelName) {
		errorRepository.save(new ReconciliationError(taskId, ReconciliationErrorType.UNKNOWN_MODEL,
				buildDeviceDescriptorIdentity(deviceModelName)));
	}

	@Override
	public void saveNetworkUnavailable(UUID taskId, Set<NetworkElement> orhans) {
		Set<ReconciliationError> errors = orhans.stream().map(orhan -> {
			ReconciliationErrorType errorType = orhan.getNetworkStatus() == NetworkStatus.Undefined ?
					ReconciliationErrorType.NO_MONITORING :
					ReconciliationErrorType.NOT_DISCOVERED;

			return new ReconciliationError(taskId, errorType, buildNeIdentity(orhan.getSnmpAgentName(), orhan.getIp()));
		}).collect(Collectors.toSet());
		errorRepository.saveAll(errors);

	}

	@Override
	public ReconciliationTask initTask() {
		ReconciliationTask task = new ReconciliationTask(getCurrent(), TaskStatus.Pending);
		return taskRepository.save(task);
	}

	@Override public void startTask(ReconciliationTask task) {
		task.setTaskStatus(TaskStatus.InProgress);
		taskRepository.save(task);
	}

	@Override public void finishTask(ReconciliationTask task) {
		task.setEndDate(getCurrent());
		taskRepository.save(task);
	}

	@Override public Optional<ReconciliationTask> findLastTask() {
		return taskRepository
				.findTopByTaskStatusInOrderByStartDateDesc(Stream.of(TaskStatus.values()).collect(Collectors.toSet()));
	}

	@Override public Optional<ReconciliationTask> findLastFinished() {
		return taskRepository.findTopByTaskStatusIsOrderByEndDateDesc(TaskStatus.Finished);
	}

	@Override public Stream<ReconciliationError> getResults(ReconciliationTask task) {
		return errorRepository.findByTaskId(task.getId());
	}

	private String buildNeIdentity(String snmpAgentName, String ip) {
		StringBuilder sb = new StringBuilder("NetworkElement{");
		sb.append("snmpAgentName='").append(snmpAgentName).append('\'');
		sb.append(", ip='").append(ip).append('\'');
		sb.append('}');
		return sb.toString();
	}

	private String buildDeviceDescriptorIdentity(String fillMmodelName) {
		StringBuilder sb = new StringBuilder("DeviceModel{");
		sb.append("vendor='").append(DeviceDescriptor.extractVendor(fillMmodelName)).append('\'');
		sb.append(", modelName='").append(DeviceDescriptor.extractModel(fillMmodelName)).append('\'');
		sb.append('}');

		return sb.toString();
	}

	private Date getCurrent() {
		return Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault())).getTime();
	}
}
