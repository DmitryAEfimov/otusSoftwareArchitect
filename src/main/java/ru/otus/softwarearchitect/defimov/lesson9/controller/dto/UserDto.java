package ru.otus.softwarearchitect.defimov.lesson9.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.otus.softwarearchitect.defimov.lesson9.model.User;
import ru.otus.softwarearchitect.defimov.lesson9.model.UserProfile;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
	@JsonProperty(value = "userId")
	private UUID userId;
	@JsonProperty(value = "profile")
	private ProfileDto profileDto;

	@JsonCreator
	public UserDto(UUID userId, ProfileDto profileDto) {
		this.userId = userId;
		this.profileDto = profileDto;
	}

	public static class ProfileDto {
		@JsonProperty(value = "email")
		private String email;
		@JsonProperty(value = "firstName")
		private String firstName;
		@JsonProperty(value = "lastName")
		private String lastName;
		@JsonProperty(value = "location")
		private String location;

		@JsonCreator
		private ProfileDto(String email, String firstName, String lastNamer, String location) {
			this.email = email;
			this.firstName = firstName;
			this.lastName = lastNamer;
			this.location = location;
		}

		public String getEmail() {
			return email;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getLocation() {
			return location;
		}
	}

	public UUID getUserId() {
		return userId;
	}

	public static UserDto asDto(User user) {
		UserProfile profile = user.getProfile();

		return new UserDto(user.getId(),
				new ProfileDto(profile.getEmail(), profile.getFirstName(), profile.getLastName(),
						profile.getLocation()));
	}
}
