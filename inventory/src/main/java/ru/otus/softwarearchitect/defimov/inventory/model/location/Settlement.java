package ru.otus.softwarearchitect.defimov.inventory.model.location;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
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
 * Населенный пункт. Всегда административно подчинен {@link Region}
 */
@Entity
@Table(name = "SETTLEMENTS")
@Access(AccessType.FIELD)
@TypeDef(
		name = "pgsql_enum",
		typeClass = PostgreSQLEnumType.class
)
public class Settlement {
	private UUID id;

	@Column(name = "SETTLEMENT_NAME")
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "SETTLEMENT_TYPE")
	@Type(type = "pgsql_enum")
	private SettlementType type;

	@ManyToOne
	@JoinColumn(name = "REGION_ID", nullable = false)
	private Region region;

	@OneToMany(mappedBy = "settlement", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Set<Street> streets;

	@OneToMany(mappedBy = "settlement", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
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

	public SettlementType getType() {
		return type;
	}

	public Region getRegion() {
		return region;
	}

	public Set<Street> getStreets() {
		return streets;
	}

	public Set<Building> getBuildings() {
		return buildings;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Settlement that = (Settlement) o;

		if (!name.equals(that.name))
			return false;
		if (type != that.type)
			return false;
		return Objects.equals(region, that.region);
	}

	@Override public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (region != null ? region.hashCode() : 0);
		return result;
	}
}
