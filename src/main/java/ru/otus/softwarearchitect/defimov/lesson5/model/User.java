package ru.otus.softwarearchitect.defimov.lesson5.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "USERS")
@Access(AccessType.FIELD)
public class User {
	private Integer id;

	@Column(name = "USER_NAME", nullable = false, unique = true, precision = 256)
	private String userName;

	@Column(name = "FIRST_NAME", precision = 64)
	private String firstName;

	@Column(name = "LAST_NAME", precision = 128)
	private String lastName;

	@Column(name = "EMAIL", nullable = false, unique = true, precision = 128)
	@Pattern(regexp = "(^[_a-zA-Z][_a-zA-Z0-9.-]+@[_a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$)")
	private String email;

	@Column(name = "PHONE", precision = 32)
	private String phoneNumber;

	@Id
	@GeneratedValue(generator = "userIdSeq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(sequenceName = "USERS_ID_SEQ", name = "userIdSeq", allocationSize = 1)
	@Column(name = "ID")
	@Access(AccessType.PROPERTY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
