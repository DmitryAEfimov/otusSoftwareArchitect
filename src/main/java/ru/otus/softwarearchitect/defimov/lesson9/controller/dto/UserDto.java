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
	private UserProfileDto profile;

	@JsonCreator
	private UserDto(UUID userId, UserProfileDto profile) {
		this.userId = userId;
		this.profile = profile;
	}

	public UUID getUserId() {
		return userId;
	}

	public UserProfileDto getProfile() {
		return profile;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UserProfileDto {
		@JsonProperty(value = "email")
		private String email;
		@JsonProperty(value = "firstName")
		private String firstName;
		@JsonProperty(value = "lastName")
		private String lastName;
		@JsonProperty(value = "location")
		private String location;

		@JsonCreator
		private UserProfileDto(String email, String firstName, String lastNamer, String location) {
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

	public static UserDto asDto(User user) {
		UserProfile profile = user.getProfile();
		UserProfileDto profileDto = new UserProfileDto(profile.getEmail(), profile.getFirstName(),
				profile.getLastName(), profile.getLocation());
		return new UserDto(user.getId(), profileDto);
	}
}
