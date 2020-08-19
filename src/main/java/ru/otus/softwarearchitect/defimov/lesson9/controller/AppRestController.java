package ru.otus.softwarearchitect.defimov.lesson9.shop.restcontroller;

import io.micrometer.core.annotation.Timed;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.User;
import ru.otus.softwarearchitect.defimov.lesson9.repository.UserRepository;
import ru.otus.softwarearchitect.defimov.lesson9.shop.restcontroller.exception.UserChangeException;
import ru.otus.softwarearchitect.defimov.lesson9.shop.restcontroller.exception.UserNotFoundException;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@RestController
@Timed(value = "auth_request",
		histogram = true)
public class AppRestController {
	private final UserRepository userRepository;
	private final MessageSource messageSource;

	public AppRestController(UserRepository userRepository, MessageSource messageSource) {
		this.userRepository = userRepository;
		this.messageSource = messageSource;
	}

	@PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
	public User createUser(@RequestBody User user) {
		if (Objects.nonNull(user.getId())) {
			throw new UserChangeException(messageSource.getMessage("userIdAlreadyExists", null, Locale.US));
		}

		try {
			return userRepository.save(user);
		} catch (DataIntegrityViolationException ex) {
			throw new UserChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@PutMapping(value = "profile/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public User updateUser(@PathVariable(name = "id") int id, @RequestBody User user) {
		User entityUser = findById(id);
		user.setId(entityUser.getId());

		try {
			return userRepository.save(user);
		} catch (DataIntegrityViolationException ex) {
			throw new UserChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@DeleteMapping(value = "logout/{id}")
	public void deleteUser(@PathVariable(name = "id") int id) {
		User user = findById(id);

		try {
			userRepository.delete(user);
		} catch (DataIntegrityViolationException ex) {
			throw new UserChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@GetMapping(value = "admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> findAll() {
		return userRepository.findAll();
	}

	private User findById(int id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("userNotFound", new Object[]{id}, Locale.US)));
	}
}
