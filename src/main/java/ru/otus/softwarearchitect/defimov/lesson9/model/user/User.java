package ru.otus.softwarearchitect.defimov.lesson9.model.user;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "USERS")
@Access(AccessType.FIELD)
@NamedQueries({
		@NamedQuery(name = "findById", query = "select u from User u where u.id = :userId"),
		@NamedQuery(name = "findByEmail", query = "select u from User u where u.profile.email = :email"),
		@NamedQuery(name = "findByCredentials", query = "select u from User u join u.credentials c where c.login = :login and c.password = :password"),
		@NamedQuery(name = "findAll", query = "select u from User u"),
})
public class User {
	private UUID id;

	@OneToOne(mappedBy = "user", orphanRemoval = true)
	private Credentials credentials;
	@Embedded
	private UserProfile profile;
	@OneToMany(mappedBy = "user")
	private Set<UserGroup> userGroups;

	protected User() {
		//  JPA Only
	}

	public User(Credentials credentials, UserProfile profile,
			Set<UserGroup> userGroups) {
		this.credentials = credentials;
		this.profile = profile;
		this.userGroups = userGroups;
	}

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

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public String getPassword() {
		return credentials.getPassword();
	}

	public void setPassword(String password) {
		credentials.password = password;
	}

	public String getLogin() {
		return credentials.login;
	}

	public Set<UserGroup> getUserGroups() {
		return Collections.unmodifiableSet(userGroups);
	}

	public Set<Role> getRoles() {
		return userGroups.stream().map(userGroup -> userGroup.roles).flatMap(Collection::stream)
				.collect(Collectors.toUnmodifiableSet());
	}
}
