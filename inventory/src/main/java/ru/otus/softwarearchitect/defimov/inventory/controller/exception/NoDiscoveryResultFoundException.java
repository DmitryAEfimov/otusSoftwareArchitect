package ru.otus.softwarearchitect.defimov.inventory.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NoDiscoveryResultFoundException extends RuntimeException {
	private static final String ERR_MESSAGE = "No discovery report found";

	public NoDiscoveryResultFoundException() {
		super(ERR_MESSAGE);
	}
}
