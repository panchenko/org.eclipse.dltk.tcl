package org.eclipse.dltk.tcl.internal.ui.actions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.VariableValue;
import org.eclipse.dltk.tcl.internal.core.packages.TclVariableResolver;
import org.eclipse.dltk.tcl.internal.core.packages.TclVariableResolver.IVariableRegistry;
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
			action.setEnabled(checkEnablement(this.selection));
		}
	}

	private boolean checkEnablement(IStructuredSelection selection2) {
		Iterator iterator = selection.iterator();
		for (; iterator.hasNext();) {
			TclSourcesSourceModule module = (TclSourcesSourceModule) iterator
					.next();
			// Check for direct source references
			String originalName = module.getOriginalName();
			if (!originalName.contains("$")) {
				return false;
			}
			// Check for global variable related references
			final Map<String, VariableValue> variables = new HashMap<String, VariableValue>();
			IScriptProject scriptProject = module.getScriptProject();
			IInterpreterInstall install = null;
			try {
				install = ScriptRuntime.getInterpreterInstall(scriptProject);
			} catch (CoreException e) {
				DLTKCore.error("Failed to locate interpreter", e);
			}
			if (install != null) {
				variables.putAll(TclPackagesManager.getVariablesEMap(install)
						.map());
			}
			variables.putAll(TclPackagesManager.getVariablesEMap(
					scriptProject.getElementName()).map());
			// TODO use NOP resolver if no variables
			final TclVariableResolver variableResolver = new TclVariableResolver(
					new IVariableRegistry() {
						public String[] getValues(String name) {
							final VariableValue value = variables.get(name);
							if (value != null) {
								return new String[] { value.getValue() };
							} else {
								return null;
							}
						}
					});
			Set<String> resolve = variableResolver.resolve(originalName);
			for (String value : resolve) {
				// if( value.equals())
				if (!value.contains("$")) {
					return false; // This is resolved variable. We can't delete
									// such associations.
				}
			}
		}
		return true;
	}
}
