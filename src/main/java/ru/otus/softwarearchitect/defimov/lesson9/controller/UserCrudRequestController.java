package ru.otus.softwarearchitect.defimov.lesson9.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.lesson9.controller.dto.UserDto;
import ru.otus.softwarearchitect.defimov.lesson9.controller.exception.UserChangeException;
import ru.otus.softwarearchitect.defimov.lesson9.model.User;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserProfile;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserRepository;
import ru.otus.softwarearchitect.defimov.lesson9.service.UserProfileService;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.EmailAlreadyExistsException;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.UserNotFoundException;

import java.util.Locale;
import java.util.UUID;

@RestController
@Timed(value = "profile_request",
		histogram = true)
public class UserCrudRequestController {
	@Autowired
	private UserProfileService profileService;

	@Autowired UserRepository userRepository;

	@Autowired
	private MessageSource messageSource;

	@PutMapping(value = "/profiles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDto updateUserProfile(@PathVariable(name = "id") UUID userId,
			@RequestBody UserDto.ProfileDto dtoProfile) {
		try {
			checkIfPrincipal(userId);

			UserProfile profile = new UserProfile(dtoProfile.getEmail(),
					dtoProfile.getFirstName(), dtoProfile.getLastName(), dtoProfile.getLocation());
			User user = profileService.changeUserProfile(userId, profile);

			return UserDto.asDto(user);
		} catch (UserNotFoundException ex) {
			throw new UserChangeException(
					messageSource.getMessage("userNotFound", new Object[] { userId }, Locale.US));
		} catch (EmailAlreadyExistsException ex) {
			throw new UserChangeException(
					messageSource
							.getMessage("userEmailAlreadyExists", new Object[] { dtoProfile.getEmail() }, Locale.US));
		}
	}

	@DeleteMapping(value = "/profiles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public void deleteUser(@PathVariable(name = "id") UUID userId) {
		try {
			checkIfPrincipal(userId);
			profileService.delete(userId);
		} catch (UserNotFoundException ex) {
			throw new UserChangeException(
					messageSource.getMessage("userNotFound", new Object[] { userId }, Locale.US));
		}
	}

	@GetMapping(value = "/profiles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDto getUserProfile(@PathVariable(name = "id") UUID userId) {
		try {
			checkIfPrincipal(userId);
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

	private void checkIfPrincipal(UUID requestedUserId) throws UserNotFoundException {
		UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User requestedUser = profileService.getUser(requestedUserId);

		if (!principal.getUsername().equals(requestedUser.getUsername())) {
			throw new AccessDeniedException(
					"User '" + principal.getUsername() + "' has no access to another user profile page!");
		}
	}
}
