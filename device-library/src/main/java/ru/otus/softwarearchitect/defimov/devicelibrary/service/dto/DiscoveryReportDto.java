package ru.otus.softwarearchitect.defimov.devicelibrary.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscoveryReportDto implements Serializable {
	@JsonProperty(value = "reportId")
	UUID reportId;

	public UUID getId() {
		return reportId;
	}
}