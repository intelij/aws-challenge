package com.amazon.challenge.statistics.factories;

import com.amazon.challenge.statistics.model.Host;
import com.amazon.challenge.statistics.model.HostParseException;
import com.amazon.challenge.statistics.model.InstanceType;

/**
 * an implementation of {@link HostMaker} that uses the simple String split to
 * parse and extract host data from it String representative line
 * 
 * @author durrah
 *
 */
public class SplitHostMaker implements HostMaker {

	@Override
	public Host makeHost(String line) throws HostParseException {
		String[] items = line.split(",");
		Host host = new Host();
		try {
			// the first token <ID>
			host.setId(Integer.parseInt(items[0]));

			// the second token <INSTANCE>
			host.setInstanceType(InstanceType.fromString(items[1]));

			// the third token <TOTAL>
			int totalSlots = Integer.parseInt(items[2]);
			host.setSlotsCount(totalSlots);

			/**
			 * for the rest of the result array, calculate slots status
			 */
			int emptySlots = 0;
			String slots = "";
			for (int i = 3; i < items.length; i++) {
				slots += items[i] + ",";
				if (Integer.parseInt(items[i]) == 0)
					emptySlots++;
			}
			host.setSlotsString(slots);

			host.setEmptySlots(emptySlots);

			host.setBusySlots(totalSlots - emptySlots);
		} catch (NumberFormatException nfe) {
			throw new HostParseException("Error parsing input values", nfe);
		}
		return host;
	}

}
