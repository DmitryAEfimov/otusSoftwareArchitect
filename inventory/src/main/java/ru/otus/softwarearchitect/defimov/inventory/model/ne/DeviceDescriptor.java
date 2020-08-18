package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "DEVICE_DESCRIPTORS")
@Access(AccessType.FIELD)
public class DeviceDescriptor {
	protected UUID id;

	@Column(name = "VENDOR")
	private String vendor;

	@Column(name = "MODEL_NAME")
	private String model;

	@Column(name = "ROLE")
	private String role;

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

	protected DeviceDescriptor() {
		//		JPA
	}

	public DeviceDescriptor(String vendor, String model, String role, Integer ethPortCnt, String ethPortType,
			Integer opticalPortCnt, String opticalPortType, Integer slotCnt) {
		this.vendor = vendor;
		this.model = model;
		this.role = role;
		this.ethPortCnt = ethPortCnt;
		this.ethPortType = ethPortType;
		this.opticalPortCnt = opticalPortCnt;
		this.opticalPortType = opticalPortType;
		this.slotCnt = slotCnt;
	}

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

	public String getVendor() {
		return vendor;
	}

	public String getModel() {
		return model;
	}

	public String getRole() {
		return role;
	}

	public Integer getEthPortCnt() {
		return ethPortCnt;
	}

	public String getEthPortType() {
		return ethPortType;
	}

	public Integer getOpticalPortCnt() {
		return opticalPortCnt;
	}

	public String getOpticalPortType() {
		return opticalPortType;
	}

	public Integer getSlotCnt() {
		return slotCnt;
	}

	public static String extractVendor(String fullModelName) {
		return split(fullModelName)[0];
	}

	public static String extractModel(String fullModelName) {
		return split(fullModelName)[1];
	}

	private static String[] split(String fullModelName) {
		return fullModelName.split("\\s", 2);
	}
}
