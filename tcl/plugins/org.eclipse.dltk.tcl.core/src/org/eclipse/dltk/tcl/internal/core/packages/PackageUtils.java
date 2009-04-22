package org.eclipse.dltk.tcl.internal.core.packages;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.tcl.internal.core.packages.PackagesManager.PackageInfo;

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
		String result = packageName.replaceAll(":", "_");
		if (packageVersion != null) {
			result += packageVersion.replaceAll("\\.", "_");
		}
		return result;
	}

	public static Set<PackageInfo> getNewPackagesFromOld(Set<String> set,
			IInterpreterInstall install) {
		Set<PackageInfo> packages = new HashSet<PackageInfo>();
		PackagesManager manager = PackagesManager.getInstance();
		Set<PackageInfo> names = manager.getPackageNames(install);
		for (PackageInfo packageInfo : names) {
			if (set.contains(packageInfo.getPackageName())) {
				packages.add(packageInfo);
			}
		}
		return packages;
	}
}
