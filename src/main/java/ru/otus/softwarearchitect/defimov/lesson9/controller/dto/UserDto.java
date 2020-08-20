package ru.otus.softwarearchitect.defimov.lesson9.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.UserProfile;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
	@JsonProperty(value = "userId")
	private UUID userId;

	@JsonProperty(value = "profile")
	private UserProfileDto profile;

	public UserDto(UUID userId, UserProfile profile) {
		this.userId = userId;
		this.profile = new UserProfileDto(profile);
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

		private UserProfileDto(UserProfile profile) {
			this.email = profile.getEmail();
			this.firstName = profile.getFirstName();
			this.lastName = profile.getLastName();
			this.location = profile.getLocation();
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
}
