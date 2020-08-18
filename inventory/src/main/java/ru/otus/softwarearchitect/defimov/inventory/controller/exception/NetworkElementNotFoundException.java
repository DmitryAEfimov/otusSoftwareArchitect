package ru.otus.softwarearchitect.defimov.inventory.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NetworkElementNotFoundException extends RuntimeException {
	public NetworkElementNotFoundException(String message) {
		super(message);
	}
}
