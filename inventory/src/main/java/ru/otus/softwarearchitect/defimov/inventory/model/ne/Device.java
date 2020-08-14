package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@Access(AccessType.FIELD)
public class Device {
	protected UUID id;

	protected Device() {
		//		JPA
	}

	protected Device(UUID id, String vendor, String model) {
		this.id = id;
		this.vendor = vendor;
		this.model = model;
	}

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

	@Column(name = "VENDOR")
	private String vendor;

	@Column(name = "MODEL")
	private String model;

	public String getVendor() {
		return vendor;
	}

	public String getModel() {
		return model;
	}
}
