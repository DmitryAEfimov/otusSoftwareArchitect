package ru.otus.softwarearchitect.defimov.inventory.model.location;

import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkElement;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;
import java.util.Set;
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
	@Transient
	private GeoPoint point;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUILDING_ID")
	private Building building;

	@Column(name = "LONGITUDE")
	private Double longitude;

	@Column(name = "LATITUDE")
	private Double latitude;

	@Column(name = "DESCRIPTION")
	private String description;

	@OneToMany(mappedBy = "location")
	private Set<NetworkElement> networkElements;

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

	public Building getBuilding() {
		return building;
	}

	public GeoPoint getPoint() {
		if (point == null) {
			point = new GeoPoint(latitude, longitude);
		}

		return point;
	}

	public Set<NetworkElement> getNetworkElements() {
		return networkElements;
	}

	public void setPoint(GeoPoint point) {
		this.point = point;
		this.longitude = point.getLongitude();
		this.latitude = point.getLatitude();
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Location location = (Location) o;

		if (!Objects.equals(building, location.building))
			return false;
		if (!Objects.equals(longitude, location.longitude))
			return false;
		return Objects.equals(latitude, location.latitude);
	}

	@Override public int hashCode() {
		int result = building != null ? building.hashCode() : 0;
		result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
		result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
		return result;
	}
}
