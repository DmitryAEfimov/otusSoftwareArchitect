package ru.otus.softwarearchitect.defimov.inventory.model.reconciliation;

import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkElement;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationError;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationTask;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public interface ReconciliationRepository {
	void saveNotFoundInInventory(UUID taskId, String snmpAgentName, String ipAddress);

	void saveUnknownModel(UUID taskId, String deviceModelName);

	void saveNetworkUnavailable(UUID taskId, Set<NetworkElement> orhans);

	ReconciliationTask initTask();

	void startTask(ReconciliationTask task);

	void finishTask(ReconciliationTask task);

	Optional<ReconciliationTask> findLastTask();

	Optional<ReconciliationTask> findLastFinished();

	Stream<ReconciliationError> getResults(ReconciliationTask task);
}
