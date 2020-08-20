package ru.otus.softwarearchitect.defimov.lesson9.model.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

@Embeddable
public class UserProfile {
	@Column(name = "EMAIL", nullable = false, unique = true)
	@Pattern(regexp = "(^[_a-zA-Z][_a-zA-Z0-9.-]+@[_a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$)")
	private String email;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "LOCATION")
	private String location;

	protected UserProfile() {
		//  JPA Only
	}

	public UserProfile(
			@Pattern(regexp = "(^[_a-zA-Z][_a-zA-Z0-9.-]+@[_a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$)") String email,
			String firstName, String lastName, String location) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
