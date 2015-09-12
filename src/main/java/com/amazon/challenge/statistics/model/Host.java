/**
 * 
 */
package com.amazon.challenge.statistics.model;

import com.amazon.challenge.statistics.ctrl.MostFilledPolicy;

/**
 * The Host Model, contains needed information to calculate the required
 * statistics.
 * 
 * @author durrah
 *
 */
public class Host {
	/**
	 * the host id, it is the first number in the line that represents the host
	 * status
	 */
	private int id;

	/**
	 * the instance type which this host supports, it is the second Comma
	 * Separated value in the line that represents the host status
	 */
	private InstanceType instanceType;

	/**
	 * the total count of slots of this host, the third CSV in the host status
	 * line
	 */
	private int slotsCount;

	/**
	 * the count of the host empty slots, 0 valued in the host status
	 */
	private int busySlots;

	/**
	 * the count of the host busy slots, 1 valued in the host status
	 */
	private int emptySlots;

	/**
	 * as an extra information used in display
	 */
	private String slotsString;

	/**
	 * an empty host is the host which empty slots count equals to its total
	 * slots count
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return emptySlots == slotsCount;
	}

	/**
	 * a full host is the host which busy slots count equals to its total slots
	 * count
	 * 
	 * @return
	 */
	public boolean isFull() {
		return busySlots == slotsCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBusySlots() {
		return busySlots;
	}

	public void setBusySlots(int busySlots) {
		this.busySlots = busySlots;
	}

	public int getEmptySlots() {
		return emptySlots;
	}

	public void setEmptySlots(int emptySlots) {
		this.emptySlots = emptySlots;
	}

	public InstanceType getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(InstanceType instanceType) {
		this.instanceType = instanceType;
	}

	public int getSlotsCount() {
		return slotsCount;
	}

	public void setSlotsCount(int slotsCount) {
		this.slotsCount = slotsCount;
	}

	public String getSlotsString() {
		return slotsString;
	}

	public void setSlotsString(String slotsString) {
		this.slotsString = slotsString;
	}

	/**
	 * a most filled host is that not empty, not full, and conforms with a
	 * policy
	 * 
	 * @param mostFilledPolicy
	 * @return true if the host is MostFilled
	 * @see MostFilledPolicy
	 */
	public boolean isMostFilled(MostFilledPolicy mostFilledPolicy) {
		if (isFull() || isEmpty())
			return false;
		return mostFilledPolicy.isMostFilled(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(id);
		sb.append(",");
		sb.append(instanceType);
		sb.append(",");
		sb.append(slotsCount);
		sb.append(":");
		sb.append(slotsString);
		sb.append(":");
		sb.append(isEmpty() ? "EMPTY" : isFull() ? "FULL" : "OCCUPIED");
		sb.append("]");
		return sb.toString();
	}

}
