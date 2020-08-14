package ru.otus.softwarearchitect.defimov.discoverymock.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "reportItems")
public class DiscoveryReportItem {
	@Id
	private String id;
	@Indexed
	private UUID reportId;
	@Indexed
	private String networkDomen;
	private String ipAddress;
	private String model;
	private ElementStatus elementStatus;

	public DiscoveryReportItem(UUID reportId, String networkDomen, String ipAddress, String model,
			ElementStatus elementStatus) {
		this.reportId = reportId;
		this.networkDomen = networkDomen;
		this.ipAddress = ipAddress;
		this.model = model;
		this.elementStatus = elementStatus;
	}

	public String getId() {
		return id;
	}

	public UUID getReportId() {
		return reportId;
	}

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

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		DiscoveryReportItem that = (DiscoveryReportItem) o;

		if (!id.equals(that.id))
			return false;
		return reportId.equals(that.reportId);
	}

	@Override public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + reportId.hashCode();
		return result;
	}
}
