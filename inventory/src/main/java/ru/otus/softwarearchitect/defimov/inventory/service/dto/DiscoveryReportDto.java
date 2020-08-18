package ru.otus.softwarearchitect.defimov.inventory.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscoveryReportDto implements Serializable {
	@JsonProperty(value = "reportId")
	private UUID reportId;

	@JsonProperty(value = "discoveredItems")
	private int elementsCount;

	public UUID getId() {
		return reportId;
	}

	public int getElementsCount() {
		return elementsCount;
	}
}
