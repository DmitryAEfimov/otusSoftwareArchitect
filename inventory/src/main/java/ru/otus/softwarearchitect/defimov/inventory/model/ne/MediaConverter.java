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

	public MediaConverter(NetworkElementId id, String vendor, String model, int portsCnt, String ethPortType,
			String opticalPortType) {
		super(id, vendor, model);
		this.portsCnt = portsCnt;
		this.ethPortType = ethPortType;
		this.opticalPortType = opticalPortType;
	}

	@Column(name = "PORTS_CNT")
	private int portsCnt;

	@Column(name = "ETH_PORT_TYPE")
	private String ethPortType;

	@Column(name = "OPTICAL_PORT_TYPE")
	private String opticalPortType;

	public int getPortsCnt() {
		return portsCnt;
	}

	public String getEthPortType() {
		return ethPortType;
	}

	public String getOpticalPortType() {
		return opticalPortType;
	}
}
