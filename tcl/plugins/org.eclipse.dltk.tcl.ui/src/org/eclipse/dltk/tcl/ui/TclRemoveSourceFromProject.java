package org.eclipse.dltk.tcl.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class TclRemoveSourceFromProject implements IObjectActionDelegate {

	private ISelection selection;

	public TclRemoveSourceFromProject() {
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		System.out.println("4");
	}

	public void run(IAction action) {
		System.out.println("test");
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
}
