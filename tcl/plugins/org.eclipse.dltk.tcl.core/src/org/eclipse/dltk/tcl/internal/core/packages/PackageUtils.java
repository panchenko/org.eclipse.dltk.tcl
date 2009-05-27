package org.eclipse.dltk.tcl.internal.core.packages;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class PackageUtils {
	public static String packagesToKey(Set<String> packages) {
		Set<String> sorted = new TreeSet<String>();
		sorted.addAll(packages);
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = sorted.iterator(); iterator.hasNext();) {
			String object = iterator.next();
			buffer.append("_").append(object);
		}
		return buffer.toString().replaceAll(":", "_");

	}

	static String packageToPath(String packageName, String packageVersion) {
		String result = packageName.replace(':', '_');
		if (packageVersion != null) {
			result += packageVersion.replace('.', '_');
		}
		return result;
	}
}
