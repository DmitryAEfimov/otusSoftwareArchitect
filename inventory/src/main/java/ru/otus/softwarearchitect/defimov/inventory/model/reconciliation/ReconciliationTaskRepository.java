package ru.otus.softwarearchitect.defimov.inventory.model.reconciliation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationTask;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.TaskStatus;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
interface ReconciliationTaskRepository extends CrudRepository<ReconciliationTask, UUID> {
	Optional<ReconciliationTask> findTopByTaskStatusIsOrderByEndDateDesc(TaskStatus status);

	Optional<ReconciliationTask> findTopByTaskStatusInOrderByStartDateDesc(Set<TaskStatus> statuses);
}