package ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "RECONSILIATION_ERROR")
@Access(AccessType.FIELD)
public class ReconciliationError {
	private UUID id;

	protected ReconciliationError() {
		//  JPA only
	}

	public ReconciliationError(UUID taskId,
			ReconciliationErrorType errorType, String objectIdentity) {
		this.taskId = taskId;
		this.errorType = errorType;
		this.objectIdentity = objectIdentity;
	}

	@Column(name = "TASK_ID")
	private UUID taskId;

	@Enumerated(EnumType.STRING)
	@Column(name = "ERROR_TYPE")
	private ReconciliationErrorType errorType;

	@Column(name = "OBJECT_IDENTITY")
	private String objectIdentity;

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

	public UUID getTaskId() {
		return taskId;
	}

	public ReconciliationErrorType getErrorType() {
		return errorType;
	}

	public String getObjectIdentity() {
		return objectIdentity;
	}
}
