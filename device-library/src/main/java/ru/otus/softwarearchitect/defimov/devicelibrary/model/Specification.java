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
@Table(name = "SPECIFICATIONS")
@TypeDef(
		name = "pgsql_enum",
		typeClass = PostgreSQLEnumType.class
)
@Access(AccessType.FIELD)
@NamedQuery(name = "Specification.findAll", query = "from Specification")
public class Specification {
	private UUID uuid;

	@Column(name = "VENDOR", nullable = false)
	private String vendor;

	@Column(name = "MODEL_NAME", nullable = false)
	private String modelName;

	@Enumerated(EnumType.STRING)
	@Column(name = "DEVICE_CLASS")
	@Type(type = "pgsql_enum")
	private DeviceClass deviceClass;

	@Column(name = "ETH_PORT_CNT")
	private Integer ethPortCnt;

	@Column(name = "ETH_PORT_TYPE")
	private String ethPortType;

	@Column(name = "OPTICAL_PORT_CNT")
	private Integer opticalPortCnt;

	@Column(name = "OPTICAL_PORT_TYPE")
	private String opticalPortType;

	@Column(name = "SLOT_CNT")
	private Integer slotCnt;

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

	public String getModelName() {
		return modelName;
	}

	public DeviceClass getDeviceClass() {
		return deviceClass;
	}

	public void setDeviceClass(DeviceClass deviceClass) {
		this.deviceClass = deviceClass;
	}

	public Integer getEthPortCnt() {
		return ethPortCnt;
	}

	public void setEthPortCnt(Integer ethPortCnt) {
		this.ethPortCnt = ethPortCnt;
	}

	public String getEthPortType() {
		return ethPortType;
	}

	public void setEthPortType(String ethPortType) {
		this.ethPortType = ethPortType;
	}

	public Integer getOpticalPortCnt() {
		return opticalPortCnt;
	}

	public void setOpticalPortCnt(Integer opticalPortCnt) {
		this.opticalPortCnt = opticalPortCnt;
	}

	public String getOpticalPortType() {
		return opticalPortType;
	}

	public void setOpticalPortType(String opticalPortType) {
		this.opticalPortType = opticalPortType;
	}

	public Integer getSlotCnt() {
		return slotCnt;
	}

	public void setSlotCnt(Integer slotCnt) {
		this.slotCnt = slotCnt;
	}
}
