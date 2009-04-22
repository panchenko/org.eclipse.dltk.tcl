package org.eclipse.dltk.tcl.internal.core.packages;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelProvider;
import org.eclipse.dltk.internal.core.ScriptProject;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterContainerHelper;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclCorePreferences;
import org.eclipse.dltk.tcl.internal.core.packages.PackagesManager.PackageInfo;

public class TclPackagesModelProvider implements IModelProvider {
	public TclPackagesModelProvider() {
	}

	public void provideModelChanges(IModelElement parentElement, List children) {
		if (!TclCorePreferences.USE_PACKAGE_CONCEPT) {
			return;
		}
		if (parentElement.getElementType() == IModelElement.SCRIPT_PROJECT) {
			ScriptProject project = (ScriptProject) parentElement;
			IInterpreterInstall install = null;
			try {
				install = ScriptRuntime.getInterpreterInstall(project);
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
				return;
			}
			if (install == null) {
				return;
			}
			Set<String> set = InterpreterContainerHelper
					.getInterpreterContainerDependencies(project);
			Set<PackageInfo> packages = new HashSet<PackageInfo>();
			PackagesManager manager = PackagesManager.getInstance();
			Set<PackageInfo> names = manager.getPackageNames(install);
			for (PackageInfo packageInfo : names) {
				if (set.contains(packageInfo.getPackageName())) {
					packages.add(packageInfo);
				}
			}
			manager.getPathsForPackages(install, packages);
			manager.getPathsForPackagesWithDeps(install, packages);
			for (String packageName : set) {
				TclPackageFragment fragment = new TclPackageFragment(
						(ScriptProject) parentElement, packageName, install);
				if (!children.contains(fragment)) {
					children.add(fragment);
				}
			}
		}
	}

	public boolean isModelChangesProvidedFor(IModelElement modelElement,
			String name) {
		if (!TclCorePreferences.USE_PACKAGE_CONCEPT) {
			return false;
		}
		if (modelElement.getElementType() == IModelElement.SCRIPT_PROJECT) {
			return true;
		}
		return false;
	}
}
