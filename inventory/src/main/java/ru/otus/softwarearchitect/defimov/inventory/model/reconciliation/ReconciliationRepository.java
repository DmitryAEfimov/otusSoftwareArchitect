package ru.otus.softwarearchitect.defimov.inventory.model.reconciliation;

import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkElement;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationError;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationTask;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ReconciliationRepository {
	void saveNotFoundInInventory(UUID taskId, String snmpAgentName, String ipAddress);

	void saveUnknownModel(UUID taskId, String deviceModelName);

	void saveNetworkUnavailable(UUID taskId, Set<NetworkElement> orhans);

	ReconciliationTask initTask(UUID discoveryReportId);

	void startTask(ReconciliationTask task);

	void finishTask(ReconciliationTask task);

	void finishTask(ReconciliationTask task, Exception ex);

	Optional<ReconciliationTask> findLastTask();

	Optional<ReconciliationTask> findLastFinished();

	Set<ReconciliationError> getResults(ReconciliationTask task);
}
