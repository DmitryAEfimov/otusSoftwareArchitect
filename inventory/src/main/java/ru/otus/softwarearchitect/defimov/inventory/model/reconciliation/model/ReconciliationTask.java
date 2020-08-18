package ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "RECONCILIATION_TASKS")
@Access(AccessType.FIELD)
@TypeDef(
		name = "pgsql_enum",
		typeClass = PostgreSQLEnumType.class
)
public class ReconciliationTask {
	private UUID id;

	@Column(name = "DISCOVERY_REPORT_ID")
	private UUID discoveryReportId;

	@Column(name = "START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	@Type(type = "pgsql_enum")
	private TaskStatus taskStatus;

	@Column(name = "STATUS_DETAIL")
	private String statusDetailInfo;

	protected ReconciliationTask() {
		//	 JPA only
	}

	public ReconciliationTask(UUID discoveryReportId, Date startDate, TaskStatus taskStatus) {
		this.discoveryReportId = discoveryReportId;
		this.startDate = startDate;
		this.taskStatus = taskStatus;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Access(AccessType.PROPERTY)
	@Column(name = "id")
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getDiscoveryReportId() {
		return discoveryReportId;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Date getStartDate() {
		return startDate;
	}

	public String getStatusDetailInfo() {
		return statusDetailInfo;
	}

	public void setStatusDetailInfo(String statusDetailInfo) {
		this.statusDetailInfo = statusDetailInfo;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ReconciliationTask that = (ReconciliationTask) o;

		return id.equals(that.id);
	}

	@Override public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
