package ru.otus.softwarearchitect.defimov.devicelibrary.model;

import java.io.Serializable;

public class SpecificationIdentity implements Serializable {
	private final String vendor;
	private final String modelName;

	public SpecificationIdentity(String vendor, String modelName) {
		this.vendor = vendor;
		this.modelName = modelName;
	}

	public String getVendor() {
		return vendor;
	}

	public String getModelName() {
		return modelName;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		SpecificationIdentity that = (SpecificationIdentity) o;

		if (!vendor.equals(that.vendor))
			return false;
		return modelName.equals(that.modelName);
	}

	@Override public int hashCode() {
		int result = vendor.hashCode();
		result = 31 * result + modelName.hashCode();
		return result;
	}

	static SpecificationIdentity splitFullModelName(String fullModelName) {
		String[] components = fullModelName.split("\\s", 2);

		return new SpecificationIdentity(components[0], components[1]);
	}

}
