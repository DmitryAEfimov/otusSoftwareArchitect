package ru.otus.softwarearchitect.defimov.lesson9.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserRole;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
				.exceptionHandling()
				.authenticationEntryPoint(new AuthenticationEntryPointImpl(HttpStatus.UNAUTHORIZED))
				.and()
				.formLogin().loginPage("/signin")
				.usernameParameter("login").passwordParameter("password")
				.successHandler(new AuthentificationLoginSuccessHandler())
				.and()
				.logout().logoutUrl("/signout")
				.invalidateHttpSession(true).logoutSuccessHandler(new AuthentificationLogoutSuccessHandler())
				.deleteCookies("JSESSIONID")
				.and()
				.authorizeRequests()
				.antMatchers("/signin", "/signout", "/signup").permitAll()
				.antMatchers("/profiles/**").hasRole(UserRole.User.getAuthority())
				.antMatchers("/admin/**").hasRole(UserRole.Administrator.getAuthority());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	@Bean
	public AuthenticationProvider getProvider() {
		return new AppAuthProvider(userDetailsService, passwordEncoder());
	}

	private static class AuthentificationLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
		public AuthentificationLoginSuccessHandler() {
			setUseReferer(true);
		}

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {
			response.setStatus(HttpServletResponse.SC_OK);
			response.sendRedirect(request.getHeader("referer"));
		}
	}

	private static class AuthentificationLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
		@Override
		public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws IOException, ServletException {
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}

	private static class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
		private HttpStatus httpStatus;

		public AuthenticationEntryPointImpl(HttpStatus httpStatus) {
			this.httpStatus = httpStatus;
		}

		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException, ServletException {
			response.setStatus(httpStatus.value());
			response.setHeader("referer", request.getRequestURI());
		}
	}
}
