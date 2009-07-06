/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;

public class GlobalVariableContentProvider implements ITreeContentProvider {

	private Viewer fViewer;

	private GlobalVariableEntry[] fVariables = new GlobalVariableEntry[0];

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		fViewer = viewer;
	}

	public Object[] getElements(Object inputElement) {
		return fVariables;
	}

	public void setVariables(GlobalVariableEntry[] vars) {
		fVariables = vars;
		fViewer.refresh();
	}

	public GlobalVariableEntry[] getVariables() {
		GlobalVariableEntry[] variables = new GlobalVariableEntry[fVariables.length];
		for (int i = 0; i < variables.length; i++) {
			variables[i] = new GlobalVariableEntry(fVariables[i]);
		}
		return variables;
	}

	/**
	 * Remove the libraries contained in the given selection.
	 */
	public void remove(Collection<?> selection) {
		List<GlobalVariableEntry> newVars = new ArrayList<GlobalVariableEntry>();
		for (int i = 0; i < fVariables.length; i++) {
			newVars.add(fVariables[i]);
		}
		for (Object element : selection) {
			if (element instanceof GlobalVariableEntry) {
				newVars.remove(element);
			}
		}
		fVariables = newVars.toArray(new GlobalVariableEntry[newVars.size()]);
		fViewer.refresh();
	}

	/**
	 * Attempts to add the given variable. Returns whether the variable was
	 * added or not (as when the user answers not to overwrite an existing
	 * variable).
	 * 
	 * @param variable
	 *            the variable to add
	 * @param oldVariable
	 *            the variable to remove if not <code>null</code>
	 * @return whether the variable was added
	 */
	public boolean replaceVariable(GlobalVariableEntry variable,
			GlobalVariableEntry oldVariable) {
		String name = variable.getName();
		List<GlobalVariableEntry> newVars = new ArrayList<GlobalVariableEntry>();
		newVars.addAll(Arrays.asList(fVariables));
		for (Iterator<GlobalVariableEntry> i = newVars.iterator(); i.hasNext();) {
			GlobalVariableEntry existingVariable = i.next();
			if (existingVariable.getName().equals(name)) {
				boolean overWrite = MessageDialog
						.openQuestion(
								fViewer.getControl().getShell(),
								TclInterpreterMessages.GlobalVariableContentProvider_overwriteTitle,
								NLS
										.bind(
												TclInterpreterMessages.GlobalVariableContentProvider_overwriteMessage,
												name));
				if (!overWrite) {
					return false;
				}
				i.remove();
				break;
			}
		}
		if (oldVariable != null) {
			newVars.remove(oldVariable);
		}
		newVars.add(variable);
		fVariables = newVars.toArray(new GlobalVariableEntry[newVars.size()]);
		return true;
	}

	public Object[] getChildren(Object parentElement) {
		return null;
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return false;
	}

}
