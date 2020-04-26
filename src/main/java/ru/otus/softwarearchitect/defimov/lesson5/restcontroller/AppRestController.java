package ru.otus.softwarearchitect.defimov.lesson5.restcontroller;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.softwarearchitect.defimov.lesson5.model.User;
import ru.otus.softwarearchitect.defimov.lesson5.repository.UserRepository;
import ru.otus.softwarearchitect.defimov.lesson5.restcontroller.exception.UserChangeException;
import ru.otus.softwarearchitect.defimov.lesson5.restcontroller.exception.UserNotFoundException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@RestController
public class AppRestController {
	private final UserRepository userRepository;
	private MessageSource messageSource;

	public AppRestController(UserRepository userRepository, MessageSource messageSource) {
		this.userRepository = userRepository;
		this.messageSource = messageSource;
	}

	@PostMapping(value = "users")
	public User createUser(@RequestBody User user) {
		try {
			checkArgument(user.getId() == null);

			return userRepository.save(user);
		} catch (DataIntegrityViolationException ex) {
			throw new UserChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		} catch (IllegalArgumentException ex) {
			throw new UserChangeException(messageSource.getMessage("userIdAlreadyExists", null, Locale.US));
		}
	}

	@PutMapping(value = "users", params = {"id"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public User updateUser(@RequestParam(name = "id") int id, @RequestBody User user) {
		User entityUser = findById(id);
		user.setId(entityUser.getId());

		try {
			return userRepository.save(user);
		} catch (DataIntegrityViolationException ex) {
			throw new UserChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@DeleteMapping(value = "users", params = {"id"})
	public void deleteUser(@RequestParam(name = "id") int id) {
		User user = findById(id);

		try {
			userRepository.delete(user);
		} catch (DataIntegrityViolationException ex) {
			throw new UserChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@GetMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@GetMapping(value = "users", params = {"id"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public User findUser(@RequestParam(name = "id") int id) {
		return findById(id);
	}

	private User findById(int id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("userNotFound", new Object[]{id}, Locale.US)));
	}
}
