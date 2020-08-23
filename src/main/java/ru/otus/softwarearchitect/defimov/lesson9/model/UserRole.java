package ru.otus.softwarearchitect.defimov.lesson9.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
	User("USER"), Administrator("ADMIN");

	private String authRole;

	UserRole(String authRole) {
		this.authRole = authRole;
	}

	@Override
	public String getAuthority() {
		return authRole;
	}
}
