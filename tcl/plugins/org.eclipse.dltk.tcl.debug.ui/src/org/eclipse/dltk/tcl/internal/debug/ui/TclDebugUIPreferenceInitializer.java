package org.eclipse.dltk.tcl.internal.debug.ui;

import org.eclipse.dltk.debug.ui.DLTKDebugUIPluginPreferenceInitializer;
import org.eclipse.dltk.tcl.core.TclNature;

public class TclDebugUIPreferenceInitializer extends
		DLTKDebugUIPluginPreferenceInitializer {

	protected String getNatureId() {
		return TclNature.NATURE_ID;
	}

}
