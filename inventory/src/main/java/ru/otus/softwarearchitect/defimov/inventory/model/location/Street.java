package ru.otus.softwarearchitect.defimov.inventory.model.location;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Улица. Практически всегда принадлежит населенному пункту {@link Settlement}.
 * Иногда может принадлежать напрямую региону {@link Region}. Например шоссе между городами
 */
@Entity
@Table(name = "STREETS")
@Access(AccessType.FIELD)
@TypeDef(
		name = "pgsql_enum",
		typeClass = PostgreSQLEnumType.class
)
public class Street {
	private UUID id;

	@Column(name = "STREET_NAME", nullable = false)
	private String name;

	@Enumerated
	@Column(name = "STREET_TYPE")
	@Type(type = "pgsql_enum")
	private RegionType type;

	@ManyToOne
	@JoinColumn(name = "REGION_ID")
	private Region region;

	@ManyToOne
	@JoinColumn(name = "SETTLEMENT_ID")
	private Settlement settlement;

	@OneToMany(mappedBy = "street", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Set<Building> buildings;

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

	public String getName() {
		return name;
	}

	public RegionType getType() {
		return type;
	}

	public Region getRegion() {
		return region;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	public Set<Building> getBuildings() {
		return buildings;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Street street = (Street) o;

		if (!name.equals(street.name))
			return false;
		if (type != street.type)
			return false;
		if (!Objects.equals(region, street.region))
			return false;
		return Objects.equals(settlement, street.settlement);
	}

	@Override public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (region != null ? region.hashCode() : 0);
		result = 31 * result + (settlement != null ? settlement.hashCode() : 0);
		return result;
	}
}
