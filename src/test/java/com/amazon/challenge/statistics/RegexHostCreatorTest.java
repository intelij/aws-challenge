package com.amazon.challenge.statistics;

import com.amazon.challenge.statistics.factories.HostMaker;
import com.amazon.challenge.statistics.factories.RegexHostMaker;
import com.amazon.challenge.statistics.model.Host;
import com.amazon.challenge.statistics.model.HostParseException;
import com.amazon.challenge.statistics.model.InstanceType;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RegexHostCreatorTest extends TestCase {

	HostMaker creator;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		creator = new RegexHostMaker();
	}

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public RegexHostCreatorTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(RegexHostCreatorTest.class);
	}

	public void testEmptyHost() throws HostParseException {
		String hostString = "11,M2,4,0,0,0,0";
		Host host = creator.makeHost(hostString);
		assertTrue(host.isEmpty());
	}

	public void testFullHost() throws HostParseException {
		String hostString = "11,M2,2,1,1";
		Host host = creator.makeHost(hostString);
		assertFalse(host.isEmpty());
	}

	public void testInstanceType() throws HostParseException {
		String hostString = "11,M3,2,1,1";
		Host host = creator.makeHost(hostString);
		assertEquals(true, host.getInstanceType() == InstanceType.M3);
	}
}
