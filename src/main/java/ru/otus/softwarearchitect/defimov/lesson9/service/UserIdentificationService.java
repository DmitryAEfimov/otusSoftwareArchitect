package ru.otus.softwarearchitect.defimov.lesson9.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.softwarearchitect.defimov.lesson9.model.Credentials;
import ru.otus.softwarearchitect.defimov.lesson9.model.User;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserRepository;

@Service
public class UserIdentificationService implements UserDetailsService {
	private final UserRepository userRepository;

	public UserIdentificationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User login(Credentials credentials) {
		return userRepository.save(new User(credentials));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findTopByProfileEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with login " + username + " not found"));
	}
}
