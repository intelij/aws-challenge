/**
 * 
 */
package com.amazon.challenge.statistics.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * holds calculation related information
 * 
 * @ThreadSafe
 * @author durrah
 *
 */
public class InstanceInfo {

	/**
	 * total number of full hosts, @ThreadSafe
	 */
	public final AtomicInteger full = new AtomicInteger();

	/**
	 * total number of empty hosts, @ThreadSafe
	 */
	public final AtomicInteger empty = new AtomicInteger();

	/**
	 * total number of most filled hosts, @ThreadSafe
	 */
	public final AtomicInteger mostFilledHosts = new AtomicInteger();

	/**
	 * total number of most filled hosts' empty slots, @ThreadSafe
	 */
	public final AtomicInteger mostFilledHostsEmptySlots = new AtomicInteger();
}
