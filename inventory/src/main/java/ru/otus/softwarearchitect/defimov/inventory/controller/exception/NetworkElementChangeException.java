package ru.otus.softwarearchitect.defimov.inventory.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NetworkElementChangeException extends RuntimeException {
	public NetworkElementChangeException(String message) {
		super(message);
	}
}
