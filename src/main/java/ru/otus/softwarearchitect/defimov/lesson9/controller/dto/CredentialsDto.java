package ru.otus.softwarearchitect.defimov.lesson9.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CredentialsDto {
	@JsonProperty(value = "login")
	private String login;

	@JsonProperty(value = "password")
	private String password;

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}
}
