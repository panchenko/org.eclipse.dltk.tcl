package org.eclipse.dltk.xotcl.core.tests.parser;

import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.tcl.internal.parser.NewTclSourceParser;

class TestTclParser extends NewTclSourceParser {

	public TestTclParser(String content) {
		setProcessorsState(false);
	}

	public char[] getFileName() {
		return "myfile.tcl".toCharArray();
	}

	public IProblemReporter getProblemReporter() {
		return null;
	}
}