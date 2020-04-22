package ru.otus.softwarearchitect.defimov.lesson5.restcontroller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.otus.softwarearchitect.defimov.lesson5.model.User;
import ru.otus.softwarearchitect.defimov.lesson5.repository.UserRepository;
import ru.otus.softwarearchitect.defimov.lesson5.restcontroller.exception.UserChangeException;
import ru.otus.softwarearchitect.defimov.lesson5.restcontroller.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@RestController
public class AppRestController {
	private final UserRepository userRepository;

	public AppRestController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping(value = "users")
	public User createUser(@RequestBody User user) {
		try {
			return userRepository.save(user);
		} catch (DataIntegrityViolationException ex) {
			throw new UserChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
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

	@GetMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@GetMapping(value = "users", params = {"id"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public User findUser(@RequestParam(name = "id") int id) {
		return findById(id);
	}

	@DeleteMapping(value = "users", params = {"id"})
	public void deleteUser(@RequestParam(name = "id") int id) {
		try {
			userRepository.deleteById(id);
		} catch (DataIntegrityViolationException | EmptyResultDataAccessException ex) {
			throw new UserChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	private User findById(int id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id=" + id));
	}
}
