package ru.otus.softwarearchitect.defimov.lesson9.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.otus.softwarearchitect.defimov.lesson9.model.User;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserProfile;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserRepository;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.EmailAlreadyExistsException;
import ru.otus.softwarearchitect.defimov.lesson9.service.exception.UserNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserProfileService {
	@Autowired
	private UserRepository userRepository;

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

	void checkEmailNotExists(UUID thisUserId, String email) throws EmailAlreadyExistsException {
		Optional<User> anotherUser = userRepository.findTopByProfileEmail(email);

		if (anotherUser.isPresent() && !anotherUser.get().getId().equals(thisUserId)) {
			throw new EmailAlreadyExistsException();
		}
	}
}
