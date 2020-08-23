package ru.otus.softwarearchitect.defimov.lesson9.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.lesson9.controller.dto.CredentialsDto;
import ru.otus.softwarearchitect.defimov.lesson9.controller.dto.RegistrationFormDto;
import ru.otus.softwarearchitect.defimov.lesson9.controller.dto.UserDto;
import ru.otus.softwarearchitect.defimov.lesson9.controller.exception.UserChangeException;
import ru.otus.softwarearchitect.defimov.lesson9.model.Credentials;
import ru.otus.softwarearchitect.defimov.lesson9.model.User;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserProfile;
import ru.otus.softwarearchitect.defimov.lesson9.service.IdentityUserService;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.EmailAlreadyExistsException;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.LoginAlreadyExistsException;

import java.util.Locale;

@RestController
@Timed(value = "identify_request",
		histogram = true)
public class IdentificationController {
	@Autowired
	private IdentityUserService identityUserService;
	@Autowired
	private MessageSource messageSource;

	@PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDto registerUser(@RequestBody RegistrationFormDto dtoRegistrationFormProfile) {
		CredentialsDto dtoCredentials = dtoRegistrationFormProfile.getCredentialsDto();
		UserDto.ProfileDto dtoProfile = dtoRegistrationFormProfile.getProfileDto();

		Credentials credentials = new Credentials(dtoCredentials.getLogin(), dtoCredentials.getPassword());
		UserProfile profile = new UserProfile(dtoProfile.getEmail(), dtoProfile.getFirstName(),
				dtoProfile.getLastName(), dtoProfile.getLocation());

		try {
			User user = identityUserService.registerUser(credentials, profile, dtoRegistrationFormProfile.getRoles());
			return UserDto.asDto(user);
		} catch (LoginAlreadyExistsException ex) {
			throw new UserChangeException(
					messageSource
							.getMessage("userLoginAlreadyExists", new Object[] { credentials.getLogin() }, Locale.US));
		} catch (EmailAlreadyExistsException ex) {
			throw new UserChangeException(
					messageSource.getMessage("userEmailAlreadyExists", new Object[] { profile.getEmail() }, Locale.US));
		}
	}

	@PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
	public String signIn() {
		return "Welcome!";
	}

	@PostMapping(value = "/signout", produces = MediaType.APPLICATION_JSON_VALUE)
	public String signOut() {
		return "Good bye!";
	}
}
