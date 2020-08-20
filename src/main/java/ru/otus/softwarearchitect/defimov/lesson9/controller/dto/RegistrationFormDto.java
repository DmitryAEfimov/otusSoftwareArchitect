package ru.otus.softwarearchitect.defimov.lesson9.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationFormDto {
	@JsonProperty(value = "credentials")
	private CredentialsDto credentials;

	@JsonProperty(value = "profile")
	private UserDto.UserProfileDto profile;

	@JsonProperty(value = "roles")
	private List<String> userGroups;

	public CredentialsDto getCredentials() {
		return credentials;
	}

	public UserDto.UserProfileDto getProfile() {
		return profile;
	}

	public List<String> getUserGroups() {
		return userGroups;
	}
}
