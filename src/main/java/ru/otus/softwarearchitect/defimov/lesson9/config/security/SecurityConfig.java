package ru.otus.softwarearchitect.defimov.lesson9.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserRole;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.authenticationProvider(getProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authenticationProvider(getProvider())
				.formLogin().loginPage("/signin").successForwardUrl("/signin")
				.usernameParameter("login").passwordParameter("password")
				.and()
				.logout().logoutUrl("/signout").invalidateHttpSession(true)
				.and()
				.authorizeRequests()
				.antMatchers("/signin", "/signout", "/signup").permitAll()
				.antMatchers("/profiles/**").hasRole(UserRole.User.getAuthority())
				.antMatchers("/admin/**").hasRole(UserRole.Administrator.getAuthority());
	}

	@Bean
	public AuthenticationProvider getProvider() {
		return new AppAuthProvider(userDetailsService);
	}
}
