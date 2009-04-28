package org.eclipse.dltk.xotcl.core.tests.parser;

import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.tcl.internal.parser.TclSourceParser;
import org.eclipse.dltk.utils.TextUtils;

class TestTclParser extends TclSourceParser {

	public TestTclParser(String content) {
		this.content = content;
		this.codeModel = TextUtils.createLineTracker(content);
		setProcessorsState(false);
	}

	public char[] getFileName() {
		return "myfile.tcl".toCharArray();
	}

	public IProblemReporter getProblemReporter() {
		return null;
	}
}