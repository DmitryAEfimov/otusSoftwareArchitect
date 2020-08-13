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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;
import java.util.UUID;

/**
 * Самая крупная единица территориального деления. Представляет  область, край, республику  и тп.
 */
@Entity
@Table(name = "REGIONS")
@Access(AccessType.FIELD)
@TypeDef(
		name = "pgsql_enum",
		typeClass = PostgreSQLEnumType.class
)
public class Region {
	private UUID id;

	@Column(name = "REGION_NAME", nullable = false)
	private String name;

	@Enumerated
	@Column(name = "REGION_TYPE")
	@Type(type = "pgsql_enum")
	public RegionType type;

	@OneToMany(mappedBy = "region", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Set<Settlement> settlements;

	@OneToMany(mappedBy = "region", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Set<Street> streets;

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

	public Set<Settlement> getSettlements() {
		return settlements;
	}

	public Set<Street> getStreets() {
		return streets;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Region region = (Region) o;

		if (!name.equals(region.name))
			return false;
		return type == region.type;
	}

	@Override public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}
}
