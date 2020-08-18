package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum NetworkStatus {
	Undefined, Active, OnHold, PreUnavailable, Unavailable;

	private static Map<String, NetworkStatus> mapping = ImmutableMap.of(
			Undefined.name().toLowerCase(), Undefined, Active.name().toLowerCase(), Active, OnHold.name().toLowerCase(),
			OnHold, PreUnavailable.name().toLowerCase(), PreUnavailable, Unavailable.name().toLowerCase(), Unavailable);

	@JsonCreator
	public static NetworkStatus fromString(String value) {
		return mapping.get(value.toLowerCase());
	}
}
