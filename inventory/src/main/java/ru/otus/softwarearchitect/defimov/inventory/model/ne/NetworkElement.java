package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import ru.otus.softwarearchitect.defimov.inventory.model.location.Location;

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
public class NetworkElement {
	private UUID id;

	@Column(name = "SNMP_AGENT", nullable = false)
	private String snmpAgentName;

	@Column(name = "IP", nullable = false)
	@Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
	private String ip;

	@ManyToOne(optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "LOCATION", nullable = false)
	private Location location;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "NETWORK_STATUS")
	private NetworkStatus networkStatus;

	@ManyToOne
	@JoinColumn(name = "DEVICE_DESCRIPTOR_ID")
	private DeviceDescriptor deviceDescriptor;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	@Access(AccessType.PROPERTY)
	public UUID getId() {
		return id;
	}

	protected void setId(UUID id) {
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

	public NetworkStatus getNetworkStatus() {
		return networkStatus;
	}

	public void setNetworkStatus(NetworkStatus status) {
		this.networkStatus = status;
	}

	public DeviceDescriptor getDeviceDescriptor() {
		return deviceDescriptor;
	}

	public void setDeviceDescriptor(DeviceDescriptor device) {
		this.deviceDescriptor = device;
	}
}
