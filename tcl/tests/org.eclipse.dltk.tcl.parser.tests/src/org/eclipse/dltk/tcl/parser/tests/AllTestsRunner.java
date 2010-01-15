/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.tcl.parser.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestsRunner {

	public static Test suite() {
		TestSuite suite = new TestSuite("org.eclipse.dltk.tcl.parser.tests");
		// $JUnit-BEGIN$
		suite.addTestSuite(NotNegativeTypeParseTests.class);
		suite.addTestSuite(TclParserTests.class);
		suite.addTestSuite(GroupParseTests.class);
		suite.addTestSuite(IntegerTypeParseTests.class);
		suite.addTestSuite(SwitchReduceTest.class);
		suite.addTestSuite(SetCommandParseTests.class);
		suite.addTestSuite(SynopsisTests.class);
		suite.addTestSuite(ShortSynopsisTests.class);
		suite.addTestSuite(CommandOutOfScopeTests.class);
		suite.addTestSuite(ErrorReportingTests.class);
		// suite.addTestSuite(PerfomanceTests.class);
		suite.addTestSuite(TclComplexArgumentParseTests.class);
		suite.addTestSuite(SwitchCommandTests.class);
		suite.addTestSuite(ProcCommandTests.class);
		suite.addTestSuite(IndexTypeParseTests.class);
		suite.addTestSuite(TclSwitchArgumentsParseTests.class);
		suite.addTestSuite(PackageCommandTests.class);
		suite.addTestSuite(DefinitionTests.class);
		suite.addTestSuite(TclGroupArgumentsParseTests.class);
		suite.addTestSuite(WhileCommandTests.class);
		suite.addTestSuite(VersionsParserTests.class);
		suite.addTestSuite(MatchPrefixTests.class);
		// suite.addTestSuite(TclCheckerDLTKErrorComparisonTests.class);
		suite.addTestSuite(PutsCommandTests.class);
		suite.addTestSuite(TclTypedArgumentsParseTests.class);
		// suite.addTestSuite(PerformanceParsingTests.class);
		suite.addTestSuite(LoadDefinitionTests.class);
		suite.addTestSuite(AfterCommandTests.class);
		suite.addTestSuite(SocketCommandTests.class);
		suite.addTestSuite(IfCommandTests.class);
		suite.addTestSuite(TclConstantsParseTests.class);
		suite.addTestSuite(NamespaceScopeProcessorTests.class);
		// $JUnit-END$
		return suite;
	}

}
