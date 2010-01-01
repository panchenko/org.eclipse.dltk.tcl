package org.eclipse.dltk.tcl.formatter.tests;

import junit.framework.TestSuite;

import org.eclipse.dltk.formatter.tests.ScriptedTest;

public class XOTclTests extends ScriptedTest {

	@SuppressWarnings("nls")
	public static TestSuite suite() {
		return new XOTclTests().createScriptedSuite(FContext.CONTEXT,
				"scripts/xotcl-class.tcl");
	}

}
