package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import org.hibernate.annotations.TypeDef;
import ru.otus.softwarearchitect.defimov.inventory.model.location.Location;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.UUID;

/**
 * Сетевой элемент.
 * Именованное эксплуатируемое оборудование, выполняющее определенную роль в сети оператора связи
 */
@Entity
@Table(name = "NETWORK_ELEMENTS")
@Access(AccessType.FIELD)
@TypeDef(name = "ne_id", typeClass = UUID.class)
public class NetworkElement {
	private UUID id;

	@Column(name = "SNMP_AGENT", nullable = false)
	private String snmpAgentName;

	@Column(name = "IP", nullable = false)
	@Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
	private String ip;

	@ManyToOne(optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "LOCATION")
	private Location location;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	@Access(AccessType.PROPERTY)
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getSnmpAgentName() {
		return snmpAgentName;
	}

	public String getIp() {
		return ip;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
