package ru.otus.softwarearchitect.defimov.devicelibrary.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum DeviceClass {
	Switch, Router, MediaConverter, MSAN;

	private static Map<String, DeviceClass> mapping = ImmutableMap.of(Switch.name().toLowerCase(), Switch,
			Router.name().toLowerCase(), Router, MediaConverter.name().toLowerCase(), MediaConverter,
			MSAN.name().toLowerCase(),
			MSAN);

	@JsonCreator
	public static DeviceClass fromString(String value) {
		return mapping.get(value.toLowerCase());
	}
}
