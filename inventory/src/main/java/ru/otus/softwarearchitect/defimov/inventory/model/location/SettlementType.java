package ru.otus.softwarearchitect.defimov.inventory.model.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum SettlementType {
	CITY, TOWN, VILLAGE;

	private static Map<String, SettlementType> mapping = ImmutableMap.of(CITY.name().toLowerCase(), CITY,
			TOWN.name().toLowerCase(), TOWN, VILLAGE.name().toLowerCase(), VILLAGE);

	@JsonCreator
	public static SettlementType fromString(String value) {
		return mapping.get(value.toLowerCase());
	}
}
