package ru.otus.softwarearchitect.defimov.devicelibrary.model;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "MODELS")
@TypeDef(
		name = "pgsql_enum",
		typeClass = PostgreSQLEnumType.class
)
@Access(AccessType.FIELD)
@NamedQuery(name = "Device.findAll", query = "from Device")
public class Device {
	private UUID uuid;

	@Column(name = "VENDOR", nullable = false)
	private String vendor;
	@Column(name = "MODEL_NAME", nullable = false)
	private String modelName;
	@Enumerated(EnumType.STRING)
	@Column(name = "DEVICE_CLASS")
	@Type(type = "pgsql_enum")
	private DeviceClass deviceClass;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	@Access(AccessType.PROPERTY)
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public DeviceClass getDeviceClass() {
		return deviceClass;
	}

	public void setDeviceClass(DeviceClass deviceClass) {
		this.deviceClass = deviceClass;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Device device = (Device) o;

		if (!vendor.equalsIgnoreCase(device.vendor))
			return false;
		return modelName.equalsIgnoreCase(device.modelName);
	}

	@Override public int hashCode() {
		int result = vendor.toLowerCase().hashCode();
		result = 31 * result + modelName.toLowerCase().hashCode();
		return result;
	}
}
