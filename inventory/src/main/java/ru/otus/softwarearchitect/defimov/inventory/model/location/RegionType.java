package ru.otus.softwarearchitect.defimov.inventory.model.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum RegionType {
	STATE, REGION, DISTRICT;

	private static Map<String, RegionType> mapping = ImmutableMap.of(STATE.name().toLowerCase(), STATE,
			REGION.name().toLowerCase(), REGION, DISTRICT.name().toLowerCase(), DISTRICT);

	@JsonCreator
	public static RegionType fromString(String value) {
		return mapping.get(value.toLowerCase());
	}
}
