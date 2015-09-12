/**
 * 
 */
package com.amazon.challenge.statistics.factories;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazon.challenge.statistics.model.Host;
import com.amazon.challenge.statistics.model.HostParseException;
import com.amazon.challenge.statistics.model.InstanceType;

/**
 * an implementation of {@link HostMaker} that uses regular expressions to parse
 * and extract host data from it String representative line
 * 
 * @author durrah
 *
 */
public class RegexHostMaker implements HostMaker {
	/**
	 * regular expression for Strings/file lines of the structure: <br/>
	 * <ID>, <INSTANCE>,<SLOTS>,<SLOT1>,<SLOT2>,<SLOT3>,....,,<SLOTN>
	 */
	private final static Pattern hostPattern = Pattern.compile("^(\\d+),(\\w+\\d+),(\\d+),((\\d+,?)+)$");
	/**
	 * regular expression to split slots status into integers
	 */
	private final static Pattern slotsPattern = Pattern.compile("\\d+");

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Host makeHost(String line) throws HostParseException {
		Host host = new Host();
		Matcher matcher = hostPattern.matcher(line);
		// if the line matches
		if (matcher.matches()) {
			try {
				// the first group is the <ID>
				host.setId(Integer.parseInt(matcher.group(1)));
				// the second group is the instance type as String
				host.setInstanceType(InstanceType.fromString(matcher.group(2)));

				// the third group is the total slots of host
				int totalSlots = Integer.parseInt(matcher.group(3));
				host.setSlotsCount(totalSlots);

				// extra information
				host.setSlotsString(matcher.group(4));

				// get empty slots
				int emptySlots = calcEmptySlots(matcher.group(4));
				host.setEmptySlots(emptySlots);

				// set busy hosts which are total - empty
				host.setBusySlots(totalSlots - emptySlots);
			} catch (NumberFormatException invalid) {
				throw new HostParseException("Error parsing input values", invalid);
			}
		} else
			throw new HostParseException("Malformed Input");
		return host;
	}

	/**
	 * parse the rest of the host information which holds the slots statuses,
	 * and sum up the number of empty slots
	 * 
	 * @param entries
	 * @return
	 */
	private int calcEmptySlots(String entries) {
		int emptySlots = 0;
		Matcher matcher = slotsPattern.matcher(entries);
		while (matcher.find()) {
			String slot = matcher.group();
			if (Integer.parseInt(slot) == 0)
				emptySlots++;
		}
		return emptySlots;
	}
}