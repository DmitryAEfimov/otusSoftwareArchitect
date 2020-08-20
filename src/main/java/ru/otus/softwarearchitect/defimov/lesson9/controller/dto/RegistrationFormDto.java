package ru.otus.softwarearchitect.defimov.lesson9.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserRole;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationFormDto {
	@JsonProperty(value = "credentials")
	private CredentialsDto credentials;

	@JsonProperty(value = "profile")
	private UserDto.UserProfileDto profile;

	@JsonProperty(value = "roles")
	private List<UserRole> userRoles;

	public CredentialsDto getCredentials() {
		return credentials;
	}

	public UserDto.UserProfileDto getProfile() {
		return profile;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}
}
