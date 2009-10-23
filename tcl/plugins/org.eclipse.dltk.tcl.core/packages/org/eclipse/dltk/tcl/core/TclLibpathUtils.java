package org.eclipse.dltk.tcl.core;

import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.LibraryLocation;

/**
 * @since 2.0
 */
public class TclLibpathUtils {
	public static final String TCLLIBPATH = "TCLLIBPATH";

	public static void addTclLibPath(InterpreterConfig config,
			LibraryLocation[] libraries, IEnvironment environment) {
		if (libraries == null) {
			return;
		}
		String currentValue = config.removeEnvVar(TCLLIBPATH);

		IPath paths[] = new IPath[libraries.length];
		int i = 0;
		for (LibraryLocation loc : libraries) {
			paths[i++] = EnvironmentPathUtils
					.getLocalPath(loc.getLibraryPath());
		}
		StringBuffer sb = new StringBuffer();
		for (i = 0; i < paths.length; ++i) {
			final IFileHandle file = config.getEnvironment().getFile(paths[i]);
			if (file != null) {
				if (sb.length() != 0) {
					sb.append(' ');
				}
				sb.append('{');
				sb.append(file.toOSString());
				sb.append('}');
			}
		}
		if (currentValue != null) {
			if (sb.length() != 0) {
				sb.append(' ');
			}
			sb.append(convertToTclLibPathFormat(currentValue));
			// sb.append(currentValue).append(" ");
		}
		if (sb.length() != 0) {
			config.addEnvVar(TCLLIBPATH, sb.toString());
		}
	}

	public static String convertToTclLibPathFormat(String currentValue) {
		currentValue = currentValue.trim();
		if (currentValue.startsWith("'") && currentValue.endsWith("'")
				&& currentValue.length() >= 2) {
			return convertToTCLLIBPATH(currentValue.substring(1, currentValue
					.length() - 1));
		}
		if (currentValue.startsWith("\"") && currentValue.endsWith("\"")
				&& currentValue.length() >= 2) {
			return convertToTCLLIBPATH(currentValue.substring(1, currentValue
					.length() - 1));
		}
		return currentValue;
	}

	public static String convertToTCLLIBPATH(String value) {
		String replacement = "%%11213@@";
		if (value.contains("\\ ")) {
			if (value.contains(replacement)) {
				replacement = replacement + System.currentTimeMillis() + "#";
			}
			value = value.replace("\\ ", replacement);
		}
		String[] values = value.split("\\s");
		StringBuffer sb = new StringBuffer();
		for (String val : values) {
			if (!(val.startsWith("{") && val.endsWith("}"))) {
				sb.append('{');
				sb.append(val.replace(replacement, "\\ "));
				sb.append('}').append(" ");
			} else {
				sb.append(val).append(" ");
			}
		}
		return sb.toString();
	}
}
