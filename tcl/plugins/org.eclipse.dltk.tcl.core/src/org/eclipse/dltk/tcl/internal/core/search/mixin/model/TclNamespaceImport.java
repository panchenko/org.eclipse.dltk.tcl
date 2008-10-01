package org.eclipse.dltk.tcl.internal.core.search.mixin.model;

import org.eclipse.dltk.tcl.internal.core.search.mixin.TclMixinModel;

public class TclNamespaceImport {
	private static final String NAMESPACE_PREFIX = TclMixinModel.NAMESPACE_PRERIX;
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
			return NAMESPACE_PREFIX + namespace + "|" + pattern;
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

	public static TclNamespaceImport parseKey(String key) {
		if (!key.startsWith(NAMESPACE_PREFIX)) {
			return null;
		}
		key = key.substring(1);
		int pos = key.indexOf("|");
		return new TclNamespaceImport(key.substring(0, pos), key
				.substring(pos + 1));
	}
}
