package ru.otus.softwarearchitect.defimov.inventory.model.location;

public class GeoPoint {
	private final Double latitude;
	private final Double longitude;

	public GeoPoint(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	Double getLatitude() {
		return latitude;
	}

	Double getLongitude() {
		return longitude;
	}
}
