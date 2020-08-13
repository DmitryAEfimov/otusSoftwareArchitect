package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import ru.otus.softwarearchitect.defimov.inventory.model.location.Location;

import javax.annotation.RegEx;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Сетевой элемент.
 * Именованное эксплуатируемое оборудование, выполняющее определенную роль в сети оператора связи
 */
@Entity
@Table(name = "NETWORK_ELEMENTS")
@Access(AccessType.FIELD)
public class NetworkElement {

	@EmbeddedId
	private NetworkElementId id;

	@Column(name = "SNMP_AGENT", nullable = false)
	private String snmpAgentName;

	@Column(name = "IP", nullable = false)
	@RegEx()
	private String ip;

	@ManyToOne(optional = false)
	@JoinColumn(name = "LOCATION")
	private Location location;

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

	public UUID getId() {
		return id.getId();
	}

	public void setId(UUID id) {
		this.id.setId(id);
	}
}
