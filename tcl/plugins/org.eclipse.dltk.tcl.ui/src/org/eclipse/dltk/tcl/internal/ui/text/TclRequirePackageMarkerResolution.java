/**
 * 
 */
package org.eclipse.dltk.tcl.internal.ui.text;

import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterContainerHelper;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IMarkerResolution;

final class TclRequirePackageMarkerResolution implements IMarkerResolution {
	private String pkgName;
	private IScriptProject project;

	public TclRequirePackageMarkerResolution(String pkgName,
			IScriptProject scriptProject) {
		this.pkgName = pkgName;
		this.project = scriptProject;
	}

	public String getLabel() {
		final String msg = Messages.TclRequirePackageMarkerResolution_addPackageToBuildpath;
		return NLS.bind(msg, pkgName);
	}

	public void run(final IMarker marker) {
		final IInterpreterInstall install;
		try {
			install = ScriptRuntime.getInterpreterInstall(project);
		} catch (CoreException e1) {
			if (DLTKCore.DEBUG) {
				e1.printStackTrace();
			}
			return;
		}
		if (install != null) {
			// Ru = new Job("Add package dependencies") {
			// protected IStatus run(IProgressMonitor monitor) {
			try {
				marker.delete();
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
			Set names = InterpreterContainerHelper
					.getInterpreterContainerDependencies(project);
			names.add(pkgName);
			InterpreterContainerHelper.setInterpreterContainerDependencies(
					project, names);
		}
	}
}
