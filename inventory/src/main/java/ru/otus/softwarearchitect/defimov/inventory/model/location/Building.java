package ru.otus.softwarearchitect.defimov.inventory.model.location;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

/**
 * Здание, расположенное на улице {@link Street}.
 * Иногда располагается прямо в населенном пункте {@link Settlement}, если последний не имеет улиц (например в дома в небольших деревнях)
 */
@Entity
@Table(name = "BUILDINGS")
@Access(AccessType.FIELD)
public class Building {
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "SETTLEMENT_ID")
	private Settlement settlement;
	@ManyToOne
	@JoinColumn(name = "STREET_ID")
	private Street street;
	@Column(name = "HOUSE_NUM")
	private int houseNumber;
	@Column(name = "HOUSE_LITERAL")
	private String houseLiteral;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Access(AccessType.PROPERTY)
	@Column(name = "ID")
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Building building = (Building) o;

		if (houseNumber != building.houseNumber)
			return false;
		if (!Objects.equals(settlement, building.settlement))
			return false;
		if (!Objects.equals(street, building.street))
			return false;
		return Objects.equals(houseLiteral, building.houseLiteral);
	}

	@Override public int hashCode() {
		int result = settlement != null ? settlement.hashCode() : 0;
		result = 31 * result + (street != null ? street.hashCode() : 0);
		result = 31 * result + houseNumber;
		result = 31 * result + (houseLiteral != null ? houseLiteral.hashCode() : 0);
		return result;
	}
}
