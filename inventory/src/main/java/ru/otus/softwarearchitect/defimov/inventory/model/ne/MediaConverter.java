package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MEDIA_CONVERTERS")
@Access(AccessType.FIELD)
public class MediaConverter extends Device {
	protected MediaConverter() {
		//		JPA
	}

	public MediaConverter(NetworkElementId id, String vendor, String model, int portsCnt, String ethPortsSpeed,
			String opticalPortsSpeed) {
		super(id, vendor, model);
		this.portsCnt = portsCnt;
		this.ethPortsSpeed = ethPortsSpeed;
		this.opticalPortsSpeed = opticalPortsSpeed;
	}

	@Column(name = "PORTS_CNT")
	private int portsCnt;

	@Column(name = "ETH_PORTS_SPEED")
	private String ethPortsSpeed;

	@Column(name = "OPTICAL_PORTS_SPEED")
	private String opticalPortsSpeed;

	public int getPortsCnt() {
		return portsCnt;
	}

	public String getEthPortsSpeed() {
		return ethPortsSpeed;
	}

	public String getOpticalPortsSpeed() {
		return opticalPortsSpeed;
	}
}
