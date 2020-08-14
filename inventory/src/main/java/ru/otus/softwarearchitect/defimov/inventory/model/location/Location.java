package ru.otus.softwarearchitect.defimov.inventory.model.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

/**
 * Местоположение. Описывается либо милицейским адресом {@link Building}, либо географическими координатами {@link GeoPoint}
 * Может иметь уточняющее описание для более точного ориентирования на местности. Например "школа №5", "от дер.Пупкино, через 300 метров съезд на проселочную дорогу")
 */
@Entity
@Table(name = "LOCATIONS")
@Access(AccessType.FIELD)
public class Location {
	private UUID id;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "LONGITUDE")
	private Double longitude;

	@Column(name = "LATITUDE")
	private Double latitude;

	@Column(name = "DESCRIPTION")
	private String description;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Access(AccessType.PROPERTY)
	@Column(name = "id")
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public String getDescription() {
		return description;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Location location = (Location) o;

		if (!Objects.equals(address, location.address))
			return false;
		if (!Objects.equals(longitude, location.longitude))
			return false;
		return Objects.equals(latitude, location.latitude);
	}

	@Override public int hashCode() {
		int result = address != null ? address.hashCode() : 0;
		result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
		result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
		return result;
	}
}
