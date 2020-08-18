package ru.otus.softwarearchitect.defimov.inventory.model.reconciliation.model;

public enum ReconciliationErrorType {
	NO_NE_FOUND("Element not found in inventory",
			"Describe network element in inventory. It will be enriched during next reconciliation"),
	UNKNOWN_MODEL("Unknown device model", "Describe device model's specification in device library"),
	NO_MONITORING("No monitoring", "Element not in operational status"),
	NOT_DISCOVERED("Element not found over network",
			"Ping network element. Some issue occurs while last network discovery");

	private String errorDescription;
	private String troubleshootAdvise;

	ReconciliationErrorType(String errorDescription, String troubleshootAdvise) {
		this.errorDescription = errorDescription;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public String getTroubleshootAdvise() {
		return troubleshootAdvise;
	}
}
