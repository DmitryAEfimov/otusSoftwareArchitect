package ru.otus.softwarearchitect.defimov.lesson9.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.lesson9.controller.dto.CredentialsDto;
import ru.otus.softwarearchitect.defimov.lesson9.model.Credentials;
import ru.otus.softwarearchitect.defimov.lesson9.model.User;
import ru.otus.softwarearchitect.defimov.lesson9.service.UserIdentificationService;

import java.util.UUID;

@RestController
@Timed(value = "auth_request",
		histogram = true)
public class AuthRequestController {
	private final UserIdentificationService identificationService;

	public AuthRequestController(UserIdentificationService identificationService, MessageSource messageSource) {
		this.identificationService = identificationService;
	}

	@PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
	public UUID signin(@RequestBody CredentialsDto dtoCredentials) {
		Credentials credentials = new Credentials(dtoCredentials.getLogin(), dtoCredentials.getPassword());
		User newUser = identificationService.login(credentials);

		return newUser.getId();
	}

	@PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	public void logout() {
		//  do nothing
	}
}