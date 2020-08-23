package ru.otus.softwarearchitect.defimov.lesson9.model;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "USERS")
@Access(AccessType.FIELD)
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class User implements UserDetails {
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

	public User(Credentials credentials, Set<UserRole> userRoles) {
		this.credentials = credentials;
		credentials.setUser(this);
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

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userRoles;
	}

	@Override
	public String getUsername() {
		return credentials.login;
	}

	@Override
	public String getPassword() {
		return credentials.password;
	}

	public void setPassword(String password) {
		this.credentials.password = password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
