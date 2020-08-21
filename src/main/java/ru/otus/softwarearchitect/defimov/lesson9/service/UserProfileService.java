package ru.otus.softwarearchitect.defimov.lesson9.service;

import com.google.common.collect.ImmutableSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.otus.softwarearchitect.defimov.lesson9.model.Credentials;
import ru.otus.softwarearchitect.defimov.lesson9.model.User;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserProfile;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserRepository;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserRole;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.EmailAlreadyExistsException;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.LoginAlreadyExistsException;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.UserNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserProfileService {
	private final static UserRole DEFAULT_USER_ROLE = UserRole.User;

	private final UserRepository userRepository;

	public UserProfileService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User registerUser(Credentials credentials, UserProfile profile, List<UserRole> roles) throws
			LoginAlreadyExistsException, EmailAlreadyExistsException {

		checkEmailNotExists(null, profile.getEmail());
		checkLoginNotExists(null, credentials.getLogin());

		User user = new User(credentials);
		user.setProfile(profile);
		user.setUserRoles(getCompleteInitialRoles(roles));

		return userRepository.save(user);
	}

	public User changeUserProfile(UUID userId, UserProfile profile)
			throws UserNotFoundException, EmailAlreadyExistsException {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		checkEmailNotExists(userId, profile.getEmail());
		user.setProfile(profile);

		return userRepository.save(user);
	}

	public void delete(UUID userId) throws UserNotFoundException {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		userRepository.delete(user);
	}

	public User getUser(UUID userId) throws UserNotFoundException {
		return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
	}

	public Page<User> getUsers(int pageNum, int chunk) {
		return userRepository.findAll(PageRequest.of(pageNum, chunk));
	}

	private void checkLoginNotExists(UUID thisUserId, String login) throws LoginAlreadyExistsException {
		Optional<User> anotherUser = userRepository.findTopByCredentialsLogin(login);

		if (anotherUser.isPresent() && !anotherUser.get().getId().equals(thisUserId)) {
			throw new LoginAlreadyExistsException();
		}
	}

	private void checkEmailNotExists(UUID thisUserId, String email) throws EmailAlreadyExistsException {
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

		return ImmutableSet.copyOf(roles);
	}
}
