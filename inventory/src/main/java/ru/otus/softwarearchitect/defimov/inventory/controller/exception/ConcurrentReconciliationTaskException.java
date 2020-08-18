package ru.otus.softwarearchitect.defimov.inventory.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ConcurrentReconciliationTaskException extends RuntimeException {
	private static final String ERR_MESSAGE = "Yet another reconciliation task in-progress. Aborted";

	public ConcurrentReconciliationTaskException() {
		super(ERR_MESSAGE);
	}
}