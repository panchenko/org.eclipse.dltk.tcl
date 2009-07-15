package org.eclipse.dltk.tcl.core;

import java.util.regex.Pattern;

import org.eclipse.dltk.core.ScriptContentDescriber;

public class TclContentDescriber extends ScriptContentDescriber {
	protected final static Pattern[] header_patterns = {
			Pattern.compile("^#!.*(tclsh|wish|expect)", Pattern.MULTILINE),
			Pattern
					.compile(
							"^#!\\s*/bin/(ba|tc)?sh\\s*\r?\n(.*\r?\n){0,4}exec .*(tclsh|expect|wish) .*",
							Pattern.MULTILINE),
			Pattern
					.compile(
							"# ;;; Local Variables?: \\*\\*\\*\\s*\r*\n# ;;; mode: t|Tcl \\*\\*\\*\\s*\r*\n# ;;; End: \\*\\*\\*",
							Pattern.MULTILINE) };

	protected Pattern[] getHeaderPatterns() {
		return header_patterns;
	}
}
