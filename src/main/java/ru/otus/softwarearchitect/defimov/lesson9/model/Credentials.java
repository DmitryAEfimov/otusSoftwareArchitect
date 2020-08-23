package ru.otus.softwarearchitect.defimov.lesson9.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "CREDENTIALS")
@Access(AccessType.FIELD)
public class Credentials {
	private UUID id;

	@OneToOne(optional = false)
	@JoinColumn(name = "USER_ID", unique = true, updatable = false)
	private User user;

	@Column(name = "LOGIN", nullable = false, updatable = false)
	String login;

	@Column(name = "PASSWORD", nullable = false)
	String password;

	protected Credentials() {
		//  JPA Only
	}

	public Credentials(String login, String password) {
		this.login = login;
		this.password = password;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	@Access(AccessType.PROPERTY)
	protected UUID getId() {
		return id;
	}

	protected void setId(UUID id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	void setUser(User user) {
		this.user = user;
	}
}
