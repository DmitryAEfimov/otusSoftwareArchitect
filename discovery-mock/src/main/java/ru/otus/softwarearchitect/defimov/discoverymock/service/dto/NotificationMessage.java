package ru.otus.softwarearchitect.defimov.discoverymock.service.dto;

import java.io.Serializable;
import java.util.UUID;

public class NotificationMessage implements Serializable {
	private final UUID reportId;
	private final int discoveredItems;

	public NotificationMessage(UUID reportId, int discoveredItems) {
		this.reportId = reportId;
		this.discoveredItems = discoveredItems;
	}

	public UUID getReportId() {
		return reportId;
	}

	public int getDiscoveredItems() {
		return discoveredItems;
	}
}
