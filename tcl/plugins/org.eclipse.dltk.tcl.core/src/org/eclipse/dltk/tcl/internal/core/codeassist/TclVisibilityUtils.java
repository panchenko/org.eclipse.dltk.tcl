package org.eclipse.dltk.tcl.internal.core.codeassist;

public class TclVisibilityUtils {
	public static boolean isPrivate(String name) {
		int pos = name.lastIndexOf("::");
		if (pos >= 0) {
			pos = pos + 2;
		} else {
			pos = 0;
		}
		if (pos < name.length()) {
			char c = name.charAt(pos);
			if (Character.isUpperCase(c) || c == '_') {
				return true;
			}
		}
		return false;
	}
}
