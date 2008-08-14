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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses( { AfterCommandTests.class, DefinitionTests.class,
		GroupParseTests.class, IfCommandTests.class, IndexTypeParseTests.class,
		IntegerTypeParseTests.class, LoadDefinitionTests.class,
		MatchPrefixTests.class, NamespaceScopeProcessorTests.class,
		NotNegativeTypeParseTests.class, PackageCommandTests.class,
		ProcCommandTests.class, PutsCommandTests.class,
		SetCommandParseTests.class, SocketCommandTests.class,
		SwitchCommandTests.class, SynopsisTests.class,
		TclComplexArgumentParseTests.class, TclConstantsParseTests.class,
		TclGroupArgumentsParseTests.class, TclParserTests.class,
		TclSwitchArgumentsParseTests.class, TclTypedArgumentsParseTests.class,
		VersionsParserTests.class, WhileCommandTests.class,
		SwitchReduceTest.class, })
public class AllTests {

}
