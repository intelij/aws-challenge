package com.amazon.challenge.statistics;

import com.amazon.challenge.statistics.factories.HostMaker;
import com.amazon.challenge.statistics.factories.SplitHostMaker;
import com.amazon.challenge.statistics.model.Host;
import com.amazon.challenge.statistics.model.HostParseException;
import com.amazon.challenge.statistics.model.InstanceType;

import junit.framework.TestCase;

public class SplitHostCreatorTest extends TestCase {
	HostMaker creator;

	public SplitHostCreatorTest(String name) {
		super(name);
		creator = new SplitHostMaker();
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testEmptyHost() throws HostParseException {
		String hostString = "11,M2,4,0,0,0,0";
		Host host = creator.makeHost(hostString);
		assertTrue(host.isEmpty());
	}

	public void testFullHost() throws HostParseException{
		String hostString = "11,M2,2,1,1";
		Host host = creator.makeHost(hostString);
		assertFalse(host.isEmpty());
	}

	public void testInstanceType() throws HostParseException{
		String hostString = "11,M3,2,1,1";
		Host host = creator.makeHost(hostString);
		assertEquals(true, host.getInstanceType() == InstanceType.M3);
	}
}
