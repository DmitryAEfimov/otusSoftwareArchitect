package ru.otus.softwarearchitect.defimov.inventory.model.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum StreetType {
	HIGHWAY, AVENUE, STREET, ALLEY, BOULEVARD;

	private static Map<String, StreetType> mapping = ImmutableMap.of(HIGHWAY.name().toLowerCase(), HIGHWAY,
			AVENUE.name().toLowerCase(), AVENUE, STREET.name().toLowerCase(), STREET, ALLEY.name().toLowerCase(), ALLEY,
			BOULEVARD.name().toLowerCase(), BOULEVARD);

	@JsonCreator
	public static StreetType fromString(String value) {
		return mapping.get(value.toLowerCase());
	}
}
