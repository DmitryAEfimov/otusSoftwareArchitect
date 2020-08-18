package ru.otus.softwarearchitect.defimov.inventory.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.inventory.controller.exception.ConcurrentReconciliationTaskException;
import ru.otus.softwarearchitect.defimov.inventory.controller.exception.NoDiscoveryResultFoundException;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.ReconciliationRepository;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationError;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.ReconciliationTask;
import ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model.TaskStatus;
import ru.otus.softwarearchitect.defimov.inventory.service.ReconciliationService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Timed(value = "reconciliation",
		histogram = true)
public class ReconciliationRequestController {
	private final ReconciliationService reconciliationService;
	private final ReconciliationRepository reconciliationRepository;

	public ReconciliationRequestController(
			ReconciliationService reconciliationService, ReconciliationRepository reconciliationRepository) {
		this.reconciliationService = reconciliationService;
		this.reconciliationRepository = reconciliationRepository;
	}

	@PostMapping(value = "reconciliation/retry", produces = MediaType.APPLICATION_JSON_VALUE)
	public void retry() throws ConcurrentReconciliationTaskException {
		Optional<ReconciliationTask> task = reconciliationRepository.findLastTask();

		if (task.isEmpty()) {
			throw new NoDiscoveryResultFoundException();
		} else if (isTaskActive(task.get())) {
			throw new ConcurrentReconciliationTaskException();
		}

		reconciliationService.start(task.get().getId(), null);
	}

	@GetMapping(value = "reconciliation/result", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ReconciliationError> getLastResult() {
		Optional<ReconciliationTask> task = reconciliationRepository.findLastFinished();

		if (task.isPresent()) {
			return reconciliationRepository.getResults(task.get()).sorted(
					Comparator.comparing(ReconciliationError::getErrorType)
							.thenComparing(ReconciliationError::getObjectIdentity)).collect(
					Collectors.toList());
		} else {
			throw new NoDiscoveryResultFoundException();
		}
	}

	private boolean isTaskActive(ReconciliationTask task) {
		return Set.of(TaskStatus.InProgress, TaskStatus.Pending)
				.contains(task.getTaskStatus());
	}
}
