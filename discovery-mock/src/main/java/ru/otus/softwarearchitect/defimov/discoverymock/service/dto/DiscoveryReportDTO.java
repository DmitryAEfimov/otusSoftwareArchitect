package ru.otus.softwarearchitect.defimov.discoverymock.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ru.otus.softwarearchitect.defimov.discoverymock.model.ElementStatus;

import java.io.Serializable;
import java.util.List;

public class DiscoveryReportDTO implements Serializable {
	@JsonUnwrapped
	@JsonProperty(value = "items")
	private List<ItemDTO> items;

	public List<ItemDTO> getItems() {
		return items;
	}

	public static class ItemDTO {
		@JsonProperty(value = "domen")
		private String networkDomen;
		@JsonProperty(value = "ipv4")
		private String ipAddress;
		@JsonProperty(value = "model")
		private String model;
		@JsonProperty(value = "status")
		private ElementStatus elementStatus;

		public String getNetworkDomen() {
			return networkDomen;
		}

		public String getIpAddress() {
			return ipAddress;
		}

		public String getModel() {
			return model;
		}

		public ElementStatus getElementStatus() {
			return elementStatus;
		}
	}
}
