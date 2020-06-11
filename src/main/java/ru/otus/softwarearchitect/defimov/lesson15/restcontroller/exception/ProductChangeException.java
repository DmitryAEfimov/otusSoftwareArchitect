package ru.otus.softwarearchitect.defimov.lesson15.restcontroller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ProductChangeException extends RuntimeException {
	public ProductChangeException(String message) {
		super(message);
	}
}