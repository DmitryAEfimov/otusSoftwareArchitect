package ru.otus.softwarearchitect.defimov.lesson9.model.credentails;

import ru.otus.softwarearchitect.defimov.lesson9.model.user.User;

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
@Table(name = "CREDENTALS")
@Access(AccessType.FIELD)
public class Credentals {
	private UUID id;

	@OneToOne(optional = false)
	@JoinColumn(name = "USER_ID", unique = true, updatable = false)
	private User user;

	@Column(name = "LOGIN", nullable = false, updatable = false)
	private String login;

	@Column(name = "PASSWORD", nullable = false)
	private String password;

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

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}
}
