package org.eclipse.dltk.tcl.internal.core.search.mixin.model;

import org.eclipse.dltk.tcl.internal.core.search.mixin.TclMixinModel;

public class TclPackage {
	private static final String PACKAGE_PREFIX = TclMixinModel.PACKAGE_PRERIX;
	public static final String REQUIRE = "require";
	/**
	 * This is for provide and ifneeded
	 */
	public static final String PROVIDE = "provide";

	private String pack = null;
	private String version = null;
	private String kind = null;

	public TclPackage(String pkg, String version, String kind) {
		this.pack = pkg;
		this.version = version;
		this.kind = kind;
	}

	public static String makeKey(String pack, String version, String kind) {
		return PACKAGE_PREFIX + kind + "|" + pack + "|"
				+ (version == null ? "" : version);
	}

	public static TclPackage parseKey(String key) {
		if (!key.startsWith(PACKAGE_PREFIX)) {
			return null;
		}
		key = key.substring(1);
		int pos = key.indexOf('|');
		int pos2 = key.lastIndexOf('|');
		return new TclPackage(key.substring(pos + 1, pos2), key.substring(0,
				pos), key.substring(0, pos));
	}

	public String getPackackeName() {
		return pack;
	}

	public void setPackageName(String pack) {
		this.pack = pack;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public static String makeSearchRequest(String provide,
			String requiredPackage) {
		return TclMixinModel.PACKAGE_PRERIX + provide + "|" + requiredPackage
				+ "|*";
	}
}
