package ru.otus.softwarearchitect.defimov.lesson9.model.user;

import ru.otus.softwarearchitect.defimov.lesson9.model.credentails.Credentals;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "USERS")
@Access(AccessType.FIELD)
public class User {
	private UUID id;

	@Column(name = "EMAIL", nullable = false, unique = true)
	@Pattern(regexp = "(^[_a-zA-Z][_a-zA-Z0-9.-]+@[_a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$)")
	private String email;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "LOCATION")
	private String location;

	@OneToOne(mappedBy = "user", orphanRemoval = true)
	private Credentals credentals;

	@OneToMany(mappedBy = "user")
	private Set<UserGroup> userGroups;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	@Access(AccessType.PROPERTY)
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Credentals getCredentals() {
		return credentals;
	}

	public Set<UserGroup> getUserGroups() {
		return Collections.unmodifiableSet(userGroups);
	}

	public Set<Role> getRoles() {
		return userGroups.stream().map(userGroup -> userGroup.roles).flatMap(Collection::stream)
				.collect(Collectors.toUnmodifiableSet());
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

	public void addRole(UserGroup userGroup) {
		userGroups.add(userGroup);
	}

	public void removeRole(UserGroup userGroup) {
		userGroups.remove(userGroup);
	}
}
