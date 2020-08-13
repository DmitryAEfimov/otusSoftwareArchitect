package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class NetworkElementId implements Serializable {
	private UUID id;

	@Access(AccessType.PROPERTY)
	@Column(name = "ID")
	UUID getId() {
		return id;
	}

	void setId(UUID id) {
		this.id = id;
	}
}
