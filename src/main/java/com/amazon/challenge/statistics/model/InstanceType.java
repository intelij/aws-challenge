package com.amazon.challenge.statistics.model;

/**
 * The instance types of the
 * 
 * @author durrah
 *
 */
public enum InstanceType {
	M1("M1"), M2("M2"), M3("M3");

	private final String type;

	private InstanceType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	/**
	 * get an instance of {@link InstanceType} using a String
	 * 
	 * @param type
	 * @return
	 * @throws IllegalArgumentException
	 *             if the input string does not have a related
	 *             {@link InstanceType} value
	 */
	public static InstanceType fromString(String type) {
		for (InstanceType tp : values())
			if (tp.getType().equals(type))
				return tp;
		throw new IllegalArgumentException("Type " + type + " not found");
	}
}
