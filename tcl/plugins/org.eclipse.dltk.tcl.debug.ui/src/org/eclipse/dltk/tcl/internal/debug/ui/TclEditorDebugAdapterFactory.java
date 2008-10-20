package org.eclipse.dltk.tcl.internal.debug.ui;

import org.eclipse.debug.ui.actions.IRunToLineTarget;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.dltk.debug.ui.ScriptEditorDebugAdapterFactory;
import org.eclipse.dltk.debug.ui.breakpoints.ScriptToggleBreakpointAdapter;
import org.eclipse.dltk.tcl.internal.debug.ui.actions.IToggleSpawnpointsTarget;

/**
 * Debug adapter factory for the Tcl editor.
 */
public class TclEditorDebugAdapterFactory extends
		ScriptEditorDebugAdapterFactory {

	public Class[] getAdapterList() {
		return new Class[] { IRunToLineTarget.class,
				IToggleBreakpointsTarget.class, IToggleSpawnpointsTarget.class };
	}

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IToggleSpawnpointsTarget.class) {
			return getBreakpointAdapter();
		} else {
			return super.getAdapter(adaptableObject, adapterType);
		}
	}

	/*
	 * @see getBreakointAdapter()
	 */
	protected ScriptToggleBreakpointAdapter getBreakpointAdapter() {
		return new TclToggleBreakpointAdapter();
	}
}
