package ru.otus.softwarearchitect.defimov.lesson9.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserRole;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationFormDto {
	@JsonProperty(value = "credentials")
	private CredentialsDto credentialsDto;
	@JsonProperty(value = "profile")
	private UserDto.ProfileDto profileDto;
	@JsonProperty(value = "roles")
	private List<UserRole> roles;

	@JsonCreator
	public RegistrationFormDto(CredentialsDto credentialsDto,
			UserDto.ProfileDto profileDto, List<UserRole> roles) {
		this.credentialsDto = credentialsDto;
		this.profileDto = profileDto;
		this.roles = roles;
	}

	public CredentialsDto getCredentialsDto() {
		return credentialsDto;
	}

	public UserDto.ProfileDto getProfileDto() {
		return profileDto;
	}

	public List<UserRole> getRoles() {
		return roles;
	}
}
