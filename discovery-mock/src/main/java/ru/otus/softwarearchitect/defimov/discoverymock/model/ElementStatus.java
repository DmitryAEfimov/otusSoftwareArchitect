package ru.otus.softwarearchitect.defimov.discoverymock.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ElementStatus {
	@JsonProperty("Active")
	ACTIVE,
	@JsonProperty("OnHold")
	ON_HOLD,
	@JsonProperty("Unavailable")
	UNAVAILABLE
}
