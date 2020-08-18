package ru.otus.softwarearchitect.defimov.inventory.model.reconciliation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationError;

import java.util.UUID;
import java.util.stream.Stream;

@Repository
interface ReconciliationErrorRepository extends CrudRepository<ReconciliationError, UUID> {
	Stream<ReconciliationError> findByTaskId(UUID taskId);
}
