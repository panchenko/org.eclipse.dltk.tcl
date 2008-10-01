package org.eclipse.dltk.tcl.core;

import java.util.regex.Pattern;

import org.eclipse.dltk.core.ScriptContentDescriber;

public class TclContentDescriber extends ScriptContentDescriber {
	protected final static Pattern[] header_patterns = {
			// Pattern.compile("#!\\s*.*tclsh", Pattern.MULTILINE),
			// Pattern.compile("#!\\s*/usr/bin/tclsh", Pattern.MULTILINE),
			// Pattern.compile("#!\\s*/usr/bin/expect", Pattern.MULTILINE),
			// Pattern.compile("#!\\s*/usr/bin/wish", Pattern.MULTILINE),
			Pattern.compile("^#!.*(tclsh.*|wish.*|expect.*).*",
					Pattern.MULTILINE),
			Pattern
					.compile(
							"# ;;; Local Variables?: \\*\\*\\*\\s*\r*\n# ;;; mode: t|Tcl \\*\\*\\*\\s*\r*\n# ;;; End: \\*\\*\\*",
							Pattern.MULTILINE),
			Pattern
					.compile(
							"^#!\\s*/bin/(ba|tc)?sh\\s*\r*\n#.*\\\\s*\r*\nexec .*tclsh .*",
							Pattern.MULTILINE),
			Pattern
					.compile(
							"^#!\\s*/bin/(ba|tc)?sh\\s*\r*\n#.*\\\\s*\r*\nexec .*expect .*",
							Pattern.MULTILINE),
			Pattern
					.compile(
							"^#!\\s*/bin/(ba|tc)?sh\\s*\r*\n#.*\\\\s*\r*\nexec .*wish.* .*",
							Pattern.MULTILINE),
			Pattern.compile(
					"^#!\\s*/bin/(ba|tc)?sh\\s*\r*\n\\s*exec .*wish.* .*",
					Pattern.MULTILINE),
			Pattern.compile(
					"^#!\\s*/bin/(ba|tc)?sh\\s*\r*\n\\s*exec .*tclsh.* .*",
					Pattern.MULTILINE),
			Pattern.compile(
					"^#!\\s*/bin/(ba|tc)?sh\\s*\r*\n\\s*exec .*expect.* .*",
					Pattern.MULTILINE) };

	protected Pattern[] getHeaderPatterns() {
		return header_patterns;
	}
}
