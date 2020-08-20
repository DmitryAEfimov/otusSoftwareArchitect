package ru.otus.softwarearchitect.defimov.lesson9.model;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "USERS")
@Access(AccessType.FIELD)
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class User {
	private UUID id;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REMOVE })
	private Credentials credentials;
	@Embedded
	private UserProfile profile;

	@Enumerated(value = EnumType.STRING)
	@ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
	@Column(name = "ROLE")
	@Type(type = "pgsql_enum")
	private Set<UserRole> userRoles;

	protected User() {
		//  JPA Only
	}

	public User(Credentials credentials, UserProfile profile,
			Set<UserRole> userRoles) {
		this.credentials = credentials;
		credentials.setUser(this);
		this.profile = profile;
		this.userRoles = userRoles;
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

	public void addRole(UserRole role) {
		userRoles.add(role);
	}

	public void removeRole(UserRole role) {
		userRoles.remove(role);
	}

	public Set<UserRole> getRoles() {
		return Collections.unmodifiableSet(userRoles);
	}
}
