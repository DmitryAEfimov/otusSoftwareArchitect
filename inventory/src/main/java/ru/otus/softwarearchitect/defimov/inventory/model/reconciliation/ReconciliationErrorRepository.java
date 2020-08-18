package ru.otus.softwarearchitect.defimov.inventory.model.reconciliation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationError;

import java.util.Set;
import java.util.UUID;

@Repository
interface ReconciliationErrorRepository extends CrudRepository<ReconciliationError, UUID> {
	Set<ReconciliationError> findByTaskId(UUID taskId);
}
