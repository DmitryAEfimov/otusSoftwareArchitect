package ru.otus.softwarearchitect.defimov.lesson9.service;

import org.springframework.stereotype.Component;
import ru.otus.softwarearchitect.defimov.lesson9.model.dictionary.DictionaryRepository;
import ru.otus.softwarearchitect.defimov.lesson9.model.dictionary.DictionaryType;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.Credentials;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.CredentialsRepository;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.User;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.UserGroup;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.UserProfile;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.UserRepository;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.EmailAlreadyExistsException;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.LoginAlreadyExistsException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserIdentificationService {
	private final static String DEFAULT_USER_GROUP_NAME = "Users";

	private final UserRepository userRepository;
	private final CredentialsRepository credentialsRepository;
	private final DictionaryRepository dictRepository;

	public UserIdentificationService(UserRepository userRepository, CredentialsRepository credentialsRepository,
			DictionaryRepository dictRepository) {
		this.userRepository = userRepository;
		this.credentialsRepository = credentialsRepository;
		this.dictRepository = dictRepository;
	}

	public User register(Credentials credentials, UserProfile profile, List<String> userGroups)
			throws EmailAlreadyExistsException, LoginAlreadyExistsException {

		checkLoginNotExists(credentials.getLogin());
		checkEmailNotExists(profile.getEmail());

		return userRepository.save(new User(credentials, profile, findUserGroups(userGroups)));
	}

	public Optional<User> updateProfile(UUID userId, UserProfile profile)
			throws EmailAlreadyExistsException {
		Optional<User> user = getUser(userId);

		if (user.isPresent()) {
			checkEmailNotExists(profile.getEmail());
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
		return userRepository.findAll();
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

	private void checkEmailNotExists(String email) throws EmailAlreadyExistsException {
		Optional<User> user = userRepository.findByEmail(email);

		if (user.isPresent()) {
			throw new EmailAlreadyExistsException();
		}
	}

	private Set<UserGroup> findUserGroups(List<String> groupNames) {
		List<String> assignedGroupNames = groupNames != null && !groupNames.isEmpty() ?
				groupNames :
				Collections.singletonList(DEFAULT_USER_GROUP_NAME);

		return dictRepository.findTopByDictionaryType(DictionaryType.USER_GROUPS).getElements()
				.stream().filter(item -> assignedGroupNames.contains(item.getValue()))
				.filter(UserGroup.class::isInstance).map(UserGroup.class::cast).collect(
						Collectors.toSet());

	}
}
