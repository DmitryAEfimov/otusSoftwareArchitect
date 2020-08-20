package ru.otus.softwarearchitect.defimov.lesson9.service;

import com.google.common.collect.Streams;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.otus.softwarearchitect.defimov.lesson9.model.Credentials;
import ru.otus.softwarearchitect.defimov.lesson9.model.CredentialsRepository;
import ru.otus.softwarearchitect.defimov.lesson9.model.User;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserProfile;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserRepository;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserRole;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.EmailAlreadyExistsException;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.LoginAlreadyExistsException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class UserIdentificationService {
	private final static UserRole DEFAULT_USER_ROLE = UserRole.User;

	private final UserRepository userRepository;
	private final CredentialsRepository credentialsRepository;

	public UserIdentificationService(UserRepository userRepository, CredentialsRepository credentialsRepository) {
		this.userRepository = userRepository;
		this.credentialsRepository = credentialsRepository;
	}

	public User register(Credentials credentials, UserProfile profile, List<UserRole> userRoles)
			throws EmailAlreadyExistsException, LoginAlreadyExistsException {

		checkLoginNotExists(credentials.getLogin());
		checkEmailNotExists(null, profile.getEmail());

		return userRepository.save(new User(credentials, profile, getCompleteInitialRoles(userRoles)));
	}

	public Optional<User> updateProfile(UUID userId, UserProfile profile)
			throws EmailAlreadyExistsException {
		Optional<User> user = getUser(userId);

		if (user.isPresent()) {
			checkEmailNotExists(userId, profile.getEmail());
			user.get().setProfile(profile);
			userRepository.save(user.get());
		}

		return user;
	}

	public Optional<User> delete(UUID userId) {
		Optional<User> user = getUser(userId);
		user.ifPresent(userRepository::delete);

		return user;
	}

	public Stream<User> getUsers() {
		return Streams.stream(userRepository.findAll());
	}

	public Optional<User> getUser(UUID userId) {
		return userRepository.findById(userId);
	}

	private void checkLoginNotExists(String login) throws LoginAlreadyExistsException {
		Optional<Credentials> credentials = credentialsRepository.findTopByLogin(login);

		if (credentials.isPresent()) {
			throw new LoginAlreadyExistsException();
		}
	}

	private void checkEmailNotExists(@Nullable UUID thisUserId, String email) throws EmailAlreadyExistsException {
		Optional<User> anotherUser = userRepository.findTopByProfileEmail(email);

		if (anotherUser.isPresent() && !anotherUser.get().getId().equals(thisUserId)) {
			throw new EmailAlreadyExistsException();
		}
	}

	private Set<UserRole> getCompleteInitialRoles(List<UserRole> userRoles) {
		Set<UserRole> roles = new HashSet<>();
		roles.add(DEFAULT_USER_ROLE);
		if (userRoles != null && !userRoles.contains(DEFAULT_USER_ROLE)) {
			roles.addAll(userRoles);
		}

		return roles;
	}
}
