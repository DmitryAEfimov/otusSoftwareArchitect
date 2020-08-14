package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ROUTERS")
@Access(AccessType.FIELD)
public class Router extends Device {
	protected Router() {
		//		JPA
	}

	public Router(NetworkElementId id, String vendor, String model, int ethPortsCnt, String ethPortsSpeed,
			int opticalPortsCnt, String opticalPortsSpeed) {
		super(id, vendor, model);
		this.ethPortsCnt = ethPortsCnt;
		this.opticalPortsCnt = opticalPortsCnt;
	}

	@Column(name = "ETH_PORTS_CNT")
	private int ethPortsCnt;

	@Column(name = "ETH_PORTS_SPEED")
	private String ethPortsSpeed;

	@Column(name = "OPTICAL_PORTS_CNT")
	private int opticalPortsCnt;

	@Column(name = "OPTICAL_PORTS_SPEED")
	private String opticalPortsSpeed;

	public int getEthPortsCnt() {
		return ethPortsCnt;
	}

	public int getOpticalPortsCnt() {
		return opticalPortsCnt;
	}

	public String getEthPortsSpeed() {
		return ethPortsSpeed;
	}

	public String getOpticalPortsSpeed() {
		return opticalPortsSpeed;
	}
}
