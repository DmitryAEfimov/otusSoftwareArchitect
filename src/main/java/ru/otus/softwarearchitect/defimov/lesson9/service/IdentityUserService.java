package ru.otus.softwarearchitect.defimov.lesson9.service;

import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.softwarearchitect.defimov.lesson9.model.Credentials;
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

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
public class IdentityUserService implements UserDetailsService {
	private final static UserRole DEFAULT_USER_ROLE = UserRole.User;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserProfileService profileService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findTopByCredentialsLogin(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		return withUsername(user.getUsername())
				.password(user.getPassword())
				.roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
				.build();
	}

	public User registerUser(Credentials credentials, UserProfile profile, List<UserRole> roles) throws
			LoginAlreadyExistsException, EmailAlreadyExistsException {

		profileService.checkEmailNotExists(null, profile.getEmail());
		checkLoginNotExists(credentials.getLogin());

		User user = new User(credentials, getCompleteInitialRoles(roles));
		user.setProfile(profile);

		return userRepository.save(user);
	}

	private void checkLoginNotExists(String login) throws LoginAlreadyExistsException {
		Optional<User> anotherUser = userRepository.findTopByCredentialsLogin(login);

		if (anotherUser.isPresent()) {
			throw new LoginAlreadyExistsException();
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
