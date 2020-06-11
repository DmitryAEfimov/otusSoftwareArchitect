package ru.otus.softwarearchitect.defimov.lesson15.restcontroller.exception;

public class UnknownPropertyException extends RuntimeException {
	public UnknownPropertyException(String message) {
		super(message);
	}
}
