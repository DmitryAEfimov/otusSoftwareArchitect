package ru.otus.softwarearchitect.defimov.lesson9.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Timed(value = "auth_request",
		histogram = true)
public class AuthRequestController {
	private final MessageSource messageSource;

	public AuthRequestController(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
