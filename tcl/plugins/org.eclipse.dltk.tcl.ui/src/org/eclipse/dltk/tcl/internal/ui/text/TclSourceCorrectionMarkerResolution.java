/**
 * 
 */
package org.eclipse.dltk.tcl.internal.ui.text;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.core.IExternalSourceModule;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackagesFactory;
import org.eclipse.dltk.tcl.core.packages.TclProjectInfo;
import org.eclipse.dltk.tcl.core.packages.TclSourceEntry;
import org.eclipse.dltk.tcl.core.packages.UserCorrection;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.ui.editor.IScriptAnnotation;
import org.eclipse.dltk.ui.environment.IEnvironmentUI;
import org.eclipse.dltk.ui.text.IAnnotationResolution;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.PlatformUI;

final class TclSourceCorrectionMarkerResolution implements IMarkerResolution,
		IAnnotationResolution {
	private String sourceName;
	private IScriptProject project;
	private ISourceModule module;

	public TclSourceCorrectionMarkerResolution(String pkgName,
			IScriptProject scriptProject, ISourceModule module) {
		this.sourceName = pkgName;
		this.project = scriptProject;
		this.module = module;
	}

	public String getLabel() {
		return "Add user specified source file location to buildpath";
	}

	private boolean resolve() {

		final IInterpreterInstall install;
		try {
			install = ScriptRuntime.getInterpreterInstall(project);
			if (install != null) {
				// Ask for user correction.

				IEnvironment env = EnvironmentManager
						.getEnvironment(this.module);
				IEnvironmentUI envUI = (IEnvironmentUI) env
						.getAdapter(IEnvironmentUI.class);
				String file = envUI.selectFile(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(),
						IEnvironmentUI.DEFAULT);
				if (file == null) {
					return false;
				}

				TclProjectInfo tclProject = TclPackagesManager
						.getTclProject(project.getElementName());
				String handle = this.module.getHandleIdentifier();
				TclModuleInfo info = tclProject.findModule(handle);
				if (info == null) {
					// This is almost impossibly situation.
					info = TclPackagesFactory.eINSTANCE.createTclModuleInfo();
					info.setHandle(handle);
					info
							.setExternal(this.module instanceof IExternalSourceModule);
					TclSourceEntry sourceEntry = TclPackagesFactory.eINSTANCE
							.createTclSourceEntry();
					sourceEntry.setStart(-1);
					sourceEntry.setEnd(-1);
					sourceEntry.setValue(sourceName);
					info.getSourced().add(sourceEntry);
					tclProject.getModules().add(info);
				}
				UserCorrection correction = TclPackagesFactory.eINSTANCE
						.createUserCorrection();
				correction.setOriginalValue(sourceName);
				correction.setUserValue(file);
				info.getSourceCorrections().add(correction);
				TclPackagesManager.save();
				// We need to fire external archives change.
				ModelManager.getModelManager().getDeltaProcessor()
						.checkExternalChanges(new IModelElement[] { project },
								new NullProgressMonitor());
			}
		} catch (CoreException e) {
			TclUI.error("require package resolve error", e); //$NON-NLS-1$
		}
		return false;
	}

	public void run(final IMarker marker) {
		resolve();
	}

	public void run(IScriptAnnotation annotation, IDocument document) {
		resolve();
	}
}
