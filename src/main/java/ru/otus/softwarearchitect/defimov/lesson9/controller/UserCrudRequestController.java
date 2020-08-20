package ru.otus.softwarearchitect.defimov.lesson9.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.lesson9.controller.dto.RegistrationFormDto;
import ru.otus.softwarearchitect.defimov.lesson9.controller.dto.UserDto;
import ru.otus.softwarearchitect.defimov.lesson9.controller.exception.UserChangeException;
import ru.otus.softwarearchitect.defimov.lesson9.controller.exception.UserNotFoundException;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.Credentials;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.User;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.UserProfile;
import ru.otus.softwarearchitect.defimov.lesson9.service.UserIdentificationService;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.EmailAlreadyExistsException;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.LoginAlreadyExistsException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Timed(value = "user_request",
		histogram = true)
public class UserCrudRequestController {
	private final UserIdentificationService userIdentificationService;
	private final MessageSource messageSource;

	public UserCrudRequestController(UserIdentificationService userIdentificationService, MessageSource messageSource) {
		this.userIdentificationService = userIdentificationService;
		this.messageSource = messageSource;
	}

	@PostMapping(value = "users/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public User createUser(@RequestBody RegistrationFormDto registrationForm) {
		Credentials credentials = new Credentials(registrationForm.getCredentials().getLogin(),
				registrationForm.getCredentials().getPassword());

		UserProfile profile = new UserProfile(registrationForm.getProfile().getEmail(),
				registrationForm.getProfile().getFirstName(), registrationForm.getProfile().getLastName(),
				registrationForm.getProfile().getLocation());

		try {
			return userIdentificationService.register(credentials, profile, registrationForm.getUserGroups());
		} catch (LoginAlreadyExistsException ex) {
			throw new UserChangeException(
					messageSource
							.getMessage("userLoginAlreadyExists", new Object[] { credentials.getLogin() }, Locale.US));
		} catch (EmailAlreadyExistsException ex) {
			throw new UserChangeException(
					messageSource.getMessage("userEmailAlreadyExists", new Object[] { profile.getEmail() }, Locale.US));
		}
	}

	@PutMapping(value = "users/profile/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserProfile(@PathVariable(name = "id") UUID userId,
			@RequestBody UserDto.UserProfileDto profileDto) {
		UserProfile profile = new UserProfile(profileDto.getEmail(), profileDto.getFirstName(),
				profileDto.getLastName(), profileDto.getLocation());
		try {
			userIdentificationService.updateProfile(userId, profile)
					.orElseThrow(() -> new UserNotFoundException(
							messageSource.getMessage("userNotFound", new Object[] { userId }, Locale.US)));
		} catch (EmailAlreadyExistsException ex) {
			throw new UserChangeException(
					messageSource
							.getMessage("userEmailAlreadyExists", new Object[] { profile.getEmail() },
									Locale.US));
		}
	}

	@DeleteMapping(value = "users/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteUser(@PathVariable(name = "id") UUID userId) {
		Optional<User> user = userIdentificationService.delete(userId);

		if (user.isEmpty()) {
			throw new UserNotFoundException(
					messageSource.getMessage("userNotFound", new Object[] { userId }, Locale.US));
		}
	}

	@GetMapping(value = "admin/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDto getProfile(@PathVariable(name = "id") UUID userId) {
		Optional<User> optionalUser = userIdentificationService.getUser(userId);
		if (optionalUser.isEmpty()) {
			throw new UserNotFoundException(
					messageSource.getMessage("userNotFound", new Object[] { userId }, Locale.US));
		}

		return optionalUser
				.map(user -> new UserDto(user.getId(), user.getProfile())).get();
	}

	@GetMapping(value = "admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserDto> findAll() {
		return userIdentificationService.getUsers()
				.map(user -> new UserDto(user.getId(), user.getProfile())).collect(Collectors.toList());
	}
}
