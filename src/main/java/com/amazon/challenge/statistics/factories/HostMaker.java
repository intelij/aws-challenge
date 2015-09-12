package com.amazon.challenge.statistics.factories;

import com.amazon.challenge.statistics.model.Host;
import com.amazon.challenge.statistics.model.HostParseException;

/**
 * make/create instances of Host class from a line that represent the status
 * (and information) of the host stored somewhere, strategy pattern
 * 
 * @author durrah
 *
 */
public interface HostMaker {

	/**
	 * make and instance of {@link Host}
	 * 
	 * @param line
	 * @return
	 * @throws HostParseException
	 *             if the String/line the represent the host has invalid format
	 */
	public Host makeHost(String line) throws HostParseException;
}
