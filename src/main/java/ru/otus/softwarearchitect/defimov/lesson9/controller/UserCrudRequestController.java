package ru.otus.softwarearchitect.defimov.lesson9.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.lesson9.controller.dto.CredentialsDto;
import ru.otus.softwarearchitect.defimov.lesson9.controller.dto.RegistrationFormDto;
import ru.otus.softwarearchitect.defimov.lesson9.controller.dto.UserDto;
import ru.otus.softwarearchitect.defimov.lesson9.controller.exception.UserChangeException;
import ru.otus.softwarearchitect.defimov.lesson9.model.Credentials;
import ru.otus.softwarearchitect.defimov.lesson9.model.User;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserProfile;
import ru.otus.softwarearchitect.defimov.lesson9.service.UserProfileService;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.EmailAlreadyExistsException;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.LoginAlreadyExistsException;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.UserNotFoundException;

import java.util.Locale;
import java.util.UUID;

@RestController
@Timed(value = "user_request",
		histogram = true)
public class UserCrudRequestController {
	private final UserProfileService profileService;
	private final PasswordEncoder passwordEncoder;
	private final MessageSource messageSource;

	public UserCrudRequestController(UserProfileService profileService, PasswordEncoder passwordEncoder,
			MessageSource messageSource) {
		this.profileService = profileService;
		this.passwordEncoder = passwordEncoder;
		this.messageSource = messageSource;
	}

	@PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDto registerUser(@RequestBody RegistrationFormDto dtoRegistrationFormProfile) {
		CredentialsDto dtoCredentials = dtoRegistrationFormProfile.getCredentialsDto();
		UserDto.ProfileDto dtoProfile = dtoRegistrationFormProfile.getProfileDto();

		String encodedPassword = passwordEncoder.encode(dtoCredentials.getPassword());
		Credentials credentials = new Credentials(dtoCredentials.getLogin(), encodedPassword);
		UserProfile profile = new UserProfile(dtoProfile.getEmail(), dtoProfile.getFirstName(),
				dtoProfile.getLastName(), dtoProfile.getLocation());

		try {
			User user = profileService.registerUser(credentials, profile, dtoRegistrationFormProfile.getRoles());
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

	@PutMapping(value = "/profiles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDto updateUserProfile(@PathVariable(name = "id") UUID userId,
			@RequestBody UserDto.ProfileDto dtoProfile) {
		UserProfile profile = new UserProfile(dtoProfile.getEmail(),
				dtoProfile.getFirstName(), dtoProfile.getLastName(), dtoProfile.getLocation());

		try {
			User user = profileService.changeUserProfile(userId, profile);

			return UserDto.asDto(user);
		} catch (UserNotFoundException ex) {
			throw new UserChangeException(
					messageSource.getMessage("userNotFound", new Object[] { userId }, Locale.US));
		} catch (EmailAlreadyExistsException ex) {
			throw new UserChangeException(
					messageSource.getMessage("userEmailAlreadyExists", new Object[] { profile.getEmail() }, Locale.US));
		}
	}

	@DeleteMapping(value = "/profiles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteUser(@PathVariable(name = "id") UUID userId) {
		try {
			profileService.delete(userId);
		} catch (UserNotFoundException ex) {
			throw new UserChangeException(
					messageSource.getMessage("userNotFound", new Object[] { userId }, Locale.US));
		}
	}

	@GetMapping(value = "/profiles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDto getUserProfile(@PathVariable(name = "id") UUID userId) {
		try {
			User user = profileService.getUser(userId);
			return UserDto.asDto(user);
		} catch (UserNotFoundException ex) {
			throw new UserChangeException(
					messageSource.getMessage("userNotFound", new Object[] { userId }, Locale.US));
		}
	}

	@GetMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<UserDto> getAllUsers(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNum,
			@RequestParam(name = "size", required = false, defaultValue = "100") int chunk) {
		return profileService.getUsers(pageNum, chunk).map(UserDto::asDto);
	}
}
