package org.eclipse.dltk.tcl.internal.debug.ui;

import org.eclipse.dltk.debug.ui.AbstractDebugUILanguageToolkit;
import org.eclipse.dltk.tcl.internal.debug.TclDebugConstants;
import org.eclipse.jface.preference.IPreferenceStore;

public class TclDebugUILangaugeToolkit extends AbstractDebugUILanguageToolkit {

	/*
	 * @see org.eclipse.dltk.debug.ui.IDLTKDebugUILanguageToolkit#getDebugModelId()
	 */
	public String getDebugModelId() {
		return TclDebugConstants.DEBUG_MODEL_ID;
	}	
	
	/*
	 * @see org.eclipse.dltk.debug.ui.IDLTKDebugUILanguageToolkit#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		return TclDebugUIPlugin.getDefault().getPreferenceStore();
	}
}
