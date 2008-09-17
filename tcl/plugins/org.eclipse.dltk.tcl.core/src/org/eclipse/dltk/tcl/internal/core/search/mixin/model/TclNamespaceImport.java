package org.eclipse.dltk.tcl.internal.core.search.mixin.model;

public class TclNamespaceImport {
	private String namespace = null;
	private String importNsName = null;

	public TclNamespaceImport(String namespace, String pattern) {
		this.namespace = namespace;
		this.importNsName = pattern;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getImportNsName() {
		return importNsName;
	}

	public static String makeKey(String namespace, String pattern) {
		if (namespace.startsWith("::")) {
			namespace = namespace.substring(2);
		}
		if (pattern != null) {
			return "@" + namespace + "|" + pattern;
		}
		return null;
	}

	public static String processPattern(String pattern) {
		if (pattern.startsWith("::")) {
			pattern = pattern.substring(2);
		}
		int pos = pattern.lastIndexOf("::");
		if (pos != -1) {
			pattern = pattern.substring(0, pos);
		} else {
			return pattern;
		}
		return pattern;
	}
}
