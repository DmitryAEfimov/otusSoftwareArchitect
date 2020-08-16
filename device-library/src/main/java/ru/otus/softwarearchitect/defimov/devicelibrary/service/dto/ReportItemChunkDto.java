package ru.otus.softwarearchitect.defimov.devicelibrary.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportItemChunkDto implements Serializable {
	@JsonProperty(value = "last")
	boolean last;

	@JsonProperty(value = "content")
	private List<DeviceModelDto> content;

	public List<DeviceModelDto> getContent() {
		return content;
	}

	public boolean isLast() {
		return last;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DeviceModelDto {
		@JsonProperty(value = "model")
		private String deviceModel;

		public String getDeviceModel() {
			return deviceModel;
		}
	}
}
