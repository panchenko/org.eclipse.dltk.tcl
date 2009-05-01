package org.eclipse.dltk.tcl.internal.core.packages;

import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelProvider;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.core.ScriptProject;
import org.eclipse.dltk.internal.launching.InterpreterContainerInitializer;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterContainerHelper;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclCorePreferences;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesFragment;

public class TclPackagesModelProvider implements IModelProvider {
	public TclPackagesModelProvider() {
	}

	public void provideModelChanges(IModelElement parentElement, List children) {
		if (parentElement.getElementType() == IModelElement.SCRIPT_PROJECT) {
			ScriptProject project = (ScriptProject) parentElement;
			// add sources fragment
			TclSourcesFragment fragment = new TclSourcesFragment(
					(ScriptProject) parentElement);
			if (!children.contains(fragment)) {
				children.add(fragment);
			}

			if (!TclCorePreferences.USE_PACKAGE_CONCEPT) {
				return;
			}
			// Add packages fragment
			Set<String> set = InterpreterContainerHelper
					.getInterpreterContainerDependencies(project);

			IInterpreterInstall install = resolveInterpreterInstall(project);
			if (install == null) {
				return;
			}
			List<TclPackageInfo> infos = TclPackagesManager.getPackageInfos(
					install, set, true);
			for (TclPackageInfo packageName : infos) {
				TclPackageFragment pfragment = new TclPackageFragment(
						(ScriptProject) parentElement, packageName.getName());
				if (!children.contains(pfragment)) {
					children.add(pfragment);
				}
			}
		}
	}

	private IInterpreterInstall resolveInterpreterInstall(ScriptProject project) {
		try {
			IInterpreterInstall install = null;
			install = ScriptRuntime.getInterpreterInstall(project);
			if (install != null) {
				return install;
			}
		} catch (CoreException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		// Try to resolve interpreter install by hands.
		// Maybe initialization problem.
		IBuildpathEntry[] buildpath = null;
		try {
			buildpath = project.getRawBuildpath();
		} catch (ModelException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
			return null;
		}
		for (IBuildpathEntry entry : buildpath) {
			IPath path = entry.getPath();
			if (path.segmentCount() > 0
					&& path.segment(0).equals(
							ScriptRuntime.INTERPRETER_CONTAINER)) {
				try {
					IInterpreterInstall interp = InterpreterContainerInitializer
							.resolveInterpreter(
									InterpreterContainerInitializer
											.getNatureFromProject(project),
									InterpreterContainerInitializer
											.getEnvironmentFromProject(project),
									path);
					if (interp != null) {
						return interp;
					}
				} catch (CoreException e) {
					if (DLTKCore.DEBUG) {
						e.printStackTrace();
					}
				}
				break;
			}
		}
		return null;
	}

	public boolean isModelChangesProvidedFor(IModelElement modelElement,
			String name) {
		// if (!TclCorePreferences.USE_PACKAGE_CONCEPT) {
		// return false;
		// }
		if (modelElement.getElementType() == IModelElement.SCRIPT_PROJECT) {
			return true;
		}
		return false;
	}
}