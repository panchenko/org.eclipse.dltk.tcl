/**
 * 
 */
package org.eclipse.dltk.tcl.internal.ui.text;

import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.internal.ui.TclInterpreterMessages;
import org.eclipse.dltk.tcl.internal.ui.preferences.ProjectBuildJob;
import org.eclipse.dltk.ui.dialogs.MultipleInputDialog;
import org.eclipse.dltk.ui.editor.IScriptAnnotation;
import org.eclipse.dltk.ui.text.IAnnotationResolution;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.PlatformUI;

final class TclGlobalVariableSourceCorrectionMarkerResolution implements
		IMarkerResolution, IAnnotationResolution {
	private String sourceName;
	private IScriptProject project;

	public TclGlobalVariableSourceCorrectionMarkerResolution(String pkgName,
			IScriptProject scriptProject) {
		this.sourceName = pkgName;
		this.project = scriptProject;
	}

	public String getLabel() {
		return "Add variable '" + this.sourceName
				+ "' to list of project global variables";
	}

	private boolean resolve() {
		MultipleInputDialog dialog = new MultipleInputDialog(PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getShell(),
				TclInterpreterMessages.GlobalVariableBlock_AddTitle);
		dialog.addLabelField("Name: " + sourceName);
		dialog.addVariablesField("Value", null, true);

		if (dialog.open() != Window.OK) {
			return false;
		}

		String value = dialog.getStringValue("Value");

		if (value != null && value.length() > 0) {
			Map<String, String> emap = TclPackagesManager.getVariables(project
					.getElementName());
			if (!emap.containsKey(sourceName)) {
				emap.put(sourceName, value);
				TclPackagesManager.setVariables(project.getElementName(), emap);
				new ProjectBuildJob(project.getProject()).schedule(500);
			}
		}

		return true;
	}

	public void run(final IMarker marker) {
		resolve();
	}

	public void run(IScriptAnnotation annotation, IDocument document) {
		resolve();
	}
}
