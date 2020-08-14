package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "MULTI_SERVICE_ACCESS_NODES")
public class Msan extends Device {
	protected Msan() {
		//		JPA
	}

	public Msan(UUID id, String vendor, String model, int dslSlotsCnt, int opticalSlotsCnt) {
		super(id, vendor, model);
		this.dslSlotsCnt = dslSlotsCnt;
		this.opticalSlotsCnt = opticalSlotsCnt;
	}

	@Column(name = "DSL_SlOTS_CNT")
	private int dslSlotsCnt;

	@Column(name = "OPTICAL_SlOTS_CNT")
	private int opticalSlotsCnt;

	public int getDslSlotsCnt() {
		return dslSlotsCnt;
	}

	public int getOpticalSlotsCnt() {
		return opticalSlotsCnt;
	}
}
