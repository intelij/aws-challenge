package com.amazon.challenge.statistics;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTest(RegexHostCreatorTest.suite());
		suite.addTestSuite(SplitHostCreatorTest.class);
		suite.addTestSuite(TokenizerHostCreatorTest.class);
		//$JUnit-END$
		return suite;
	}

}
