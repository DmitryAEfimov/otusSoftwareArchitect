package ru.otus.softwarearchitect.defimov.discoverymock.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.otus.softwarearchitect.defimov.discoverymock.model.ElementStatus;

import java.util.Set;

public class DiscoveryReportDTO {
	private Set<ItemDTO> items;

	public static class ItemDTO {
		public final String networkDomen;
		public final String ipAddress;
		public final String model;
		public final ElementStatus elementStatus;

		@JsonCreator
		public ItemDTO(@JsonProperty(value = "domen") String networkDomen,
				@JsonProperty(value = "ipv4") String ipAddress, String model,
				@JsonProperty(value = "status") ElementStatus elementStatus) {
			this.networkDomen = networkDomen;
			this.ipAddress = ipAddress;
			this.model = model;
			this.elementStatus = elementStatus;
		}
	}

	public Set<ItemDTO> getItems() {
		return items;
	}
}
