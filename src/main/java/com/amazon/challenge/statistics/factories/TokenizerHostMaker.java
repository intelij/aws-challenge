/**
 * 
 */
package com.amazon.challenge.statistics.factories;

import java.util.StringTokenizer;

import com.amazon.challenge.statistics.model.Host;
import com.amazon.challenge.statistics.model.HostParseException;
import com.amazon.challenge.statistics.model.InstanceType;

/**
 * 
 * an implementation of {@link HostMaker} that uses String tokenizer to parse
 * and extract host data from it String representative line
 * 
 * @author durrah
 *
 */
public class TokenizerHostMaker implements HostMaker {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Host makeHost(String line) throws HostParseException {
		StringTokenizer tokenizer = new StringTokenizer(line, ",");
		Host host = new Host();
		// to check special positions (tokens)
		int i = 0;

		int totalSlots = 0;
		int emptySlots = 0;

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken().trim();
			try {
				// the first token <ID>
				if (i == 0)
					host.setId(Integer.parseInt(token));

				// the second token <INSTANCE>
				if (i == 1)
					host.setInstanceType(InstanceType.fromString(token));

				// the third token <TOTAL>
				if (i == 2) {
					totalSlots = Integer.parseInt(token);
					host.setSlotsCount(totalSlots);
				}

				/**
				 * as we parsed the first values, we skip them and start
				 * collecting slots status
				 */
				if (i > 2) {
					if (Integer.parseInt(token) == 0)
						emptySlots++;
				}

				i++;

				host.setEmptySlots(emptySlots);

				host.setBusySlots(totalSlots - emptySlots);
			} catch (NumberFormatException nfe) {
				throw new HostParseException("Error parsing input values", nfe);
			} catch (IllegalArgumentException iae) {
				throw new HostParseException("Error parsing input values", iae);
			}
		}
		return host;
	}
}
