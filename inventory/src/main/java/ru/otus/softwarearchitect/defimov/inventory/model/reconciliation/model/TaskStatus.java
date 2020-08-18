package ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum TaskStatus {
	Pending, InProgress, Finished, Aborted, Error;

	private static Map<String, TaskStatus> mapping = ImmutableMap.of(Pending.name().toLowerCase(), Pending,
			InProgress.name().toLowerCase(), InProgress, Finished.name().toLowerCase(), Finished,
			Aborted.name().toLowerCase(), Aborted, Error.name().toLowerCase(), Error);

	@JsonCreator
	public static TaskStatus fromString(String value) {
		return mapping.get(value.toLowerCase());
	}
}
