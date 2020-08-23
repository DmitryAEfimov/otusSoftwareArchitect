package ru.otus.softwarearchitect.defimov.lesson9.config.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AppAuthProvider extends DaoAuthenticationProvider {
	public AppAuthProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		setUserDetailsService(userDetailsService);
		setPasswordEncoder(passwordEncoder);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String name = auth.getName();

		try {
			UserDetails user = getUserDetailsService().loadUserByUsername(name);
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		} catch (UsernameNotFoundException ex) {
			throw new BadCredentialsException("Username/Password does not match for " + auth.getPrincipal());
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
}
