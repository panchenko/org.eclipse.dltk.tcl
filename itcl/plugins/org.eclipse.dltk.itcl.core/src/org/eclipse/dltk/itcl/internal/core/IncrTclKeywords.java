package org.eclipse.dltk.itcl.internal.core;

import org.eclipse.dltk.tcl.core.ITclKeywords;

public class IncrTclKeywords implements ITclKeywords {
	private static String[] IncrTclKeywords = { "class", "itcl", "body",
			"code", "configbody", "delete", "ensemble", "scope", "part",
			"constructor", "destructor", "common", "public", "protected",
			"private", "method", "itcl::class", "itcl::body", "itcl::code",
			"itcl::configbody", "itcl::delete", "itcl::ensemble", "itcl::scope" };

	public String[] getKeywords() {
		return IncrTclKeywords;
	}

	public String[] getKeywords(int type) {
		return IncrTclKeywords;
	}
}
