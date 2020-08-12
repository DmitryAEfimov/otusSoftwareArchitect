package ru.otus.softwarearchitect.defimov.devicelibrary.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DeviceChangeException extends RuntimeException {
	public DeviceChangeException(String message) {
		super(message);
	}
}
