package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Device {
	@EmbeddedId
	private NetworkElementId id;

	protected Device() {
		//		JPA
	}

	protected Device(NetworkElementId id, String vendor, String model) {
		this.id = id;
		this.vendor = vendor;
		this.model = model;
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
