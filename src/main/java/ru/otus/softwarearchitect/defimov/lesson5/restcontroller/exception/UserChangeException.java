package ru.otus.softwarearchitect.defimov.lesson5.restcontroller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserChangeException extends RuntimeException {
	public UserChangeException(String message) {
		super(message);
	}
}
