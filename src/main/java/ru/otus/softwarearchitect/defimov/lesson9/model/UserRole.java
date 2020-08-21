package ru.otus.softwarearchitect.defimov.lesson9.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
	User("USER"), Administrator("ADMIN");

	private String authRole;

	UserRole(String authRole) {
		this.authRole = "ROLE_" + authRole;
	}

	@Override public String getAuthority() {
		return authRole;
	}
}
