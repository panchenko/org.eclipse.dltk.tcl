/**
 * 
 */
package org.eclipse.dltk.tcl.internal.ui.text;

import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterContainerHelper;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.ui.text.IAnnotationResolution;
import org.eclipse.jface.text.BadLocationException;
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
				final Set names = InterpreterContainerHelper
						.getInterpreterContainerDependencies(project);
				if (names.add(pkgName)) {
					InterpreterContainerHelper
							.setInterpreterContainerDependencies(project, names);
					return true;
				}
			}
		} catch (CoreException e) {
			TclUI.error("require package resolve error", e); //$NON-NLS-1$
		}
		return false;
	}

	public void run(final IMarker marker) {
		resolve();
	}

	public void run(Annotation annotation, IDocument document) {
		if (resolve()) {
			// FIXME make proper solution
			if (document.getLength() > 0) {
				try {
					document.replace(0, 1, document.get(0, 1));
				} catch (BadLocationException e) {
					// e.printStackTrace();
				}
			}
		}
	}
}
