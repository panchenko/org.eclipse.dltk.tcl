package org.eclipse.dltk.tcl.internal.ui.actions;

import java.util.Iterator;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.tcl.internal.core.packages.DefaultVariablesRegistry;
import org.eclipse.dltk.tcl.internal.core.packages.TclVariableResolver;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesSourceModule;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class RemoveSourceAssociation implements IWorkbenchWindowActionDelegate {

	private IStructuredSelection selection;

	public RemoveSourceAssociation() {
	}

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
	}

	public void run(IAction action) {
		if (selection == null) {
			return;
		}
		Iterator iterator = selection.iterator();
		for (; iterator.hasNext();) {
			ISourceModule element = (ISourceModule) iterator.next();
			try {
				element.delete(false, new NullProgressMonitor());
			} catch (ModelException e) {
				DLTKCore.error("Failed to remove source association", e);
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection != null && selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) selection;
			action.setEnabled(checkEnablement());
		}
	}

	private boolean checkEnablement() {
		for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
			TclSourcesSourceModule module = (TclSourcesSourceModule) iterator
					.next();
			// Check for direct source references
			String originalName = module.getOriginalName();
			if (!originalName.contains("$")) {
				return false;
			}
			// Check for global variable related references

			IScriptProject scriptProject = ((ISourceModule) module)
					.getScriptProject();
			TclVariableResolver variableResolver = new TclVariableResolver(
					new DefaultVariablesRegistry(scriptProject));
			String value = variableResolver.resolve(originalName);

			// if( value.equals())
			if (value != null && !value.contains("$")) {
				return false; // This is resolved variable. We can't delete
				// such associations.
			}
		}
		return true;
	}
}
