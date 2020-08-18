package ru.otus.softwarearchitect.defimov.inventory.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkStatus;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportItemChunkDto implements Serializable {
	@JsonProperty(value = "last")
	boolean last;

	@JsonProperty(value = "content")
	private List<Item> content;

	public List<Item> getContent() {
		return content;
	}

	public boolean isLast() {
		return last;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Item {
		@JsonProperty(value = "networkDomen")
		private String snmpAgentName;

		@JsonProperty(value = "ipAddress")
		private String ip;

		@JsonProperty(value = "model")
		private String deviceModel;

		@JsonProperty("elementStatus")
		private NetworkStatus elementStatus;

		public String getSnmpAgentName() {
			return snmpAgentName;
		}

		public String getIp() {
			return ip;
		}

		public String getDeviceModel() {
			return deviceModel;
		}

		public NetworkStatus getElementStatus() {
			return elementStatus;
		}
	}
}
