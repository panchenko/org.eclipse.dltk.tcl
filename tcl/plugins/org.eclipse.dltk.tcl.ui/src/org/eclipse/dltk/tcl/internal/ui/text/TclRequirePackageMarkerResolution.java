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
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.ui.text.IAnnotationResolution;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IMarkerResolution;

final class TclRequirePackageMarkerResolution implements IMarkerResolution,
		IAnnotationResolution {
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

	private boolean resolve() {
		final IInterpreterInstall install;
		try {
			install = ScriptRuntime.getInterpreterInstall(project);
			if (install != null) {
				Set names = InterpreterContainerHelper
						.getInterpreterContainerDependencies(project);
				names.add(pkgName);
				InterpreterContainerHelper.setInterpreterContainerDependencies(
						project, names);
				return true;
			}
		} catch (CoreException e) {
			TclUI.error("require package resolve error", e); //$NON-NLS-1$
		}
		return false;
	}

	public void run(final IMarker marker) {
		if (resolve()) {
			try {
				marker.delete();
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean run(Annotation annotation, IDocument document) {
		return resolve();
	}
}
