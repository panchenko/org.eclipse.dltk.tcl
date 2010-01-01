package org.eclipse.dltk.tcl.formatter.tests;

import junit.framework.TestSuite;

import org.eclipse.dltk.formatter.tests.ScriptedTest;

public class IncrTclTests extends ScriptedTest {

	@SuppressWarnings("nls")
	public static TestSuite suite() {
		return new IncrTclTests().createScriptedSuite(FContext.CONTEXT,
				"scripts/itcl-class.tcl");
	}

}
