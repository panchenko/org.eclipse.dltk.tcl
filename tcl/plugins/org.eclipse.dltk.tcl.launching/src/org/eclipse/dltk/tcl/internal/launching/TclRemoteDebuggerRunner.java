package org.eclipse.dltk.tcl.internal.launching;

import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.RemoteDebuggingEngineRunner;
import org.eclipse.dltk.tcl.internal.debug.TclDebugPlugin;

public class TclRemoteDebuggerRunner extends RemoteDebuggingEngineRunner {

	public TclRemoteDebuggerRunner(IInterpreterInstall install) {
		super(install);
	}

	@Override
	protected String getDebugPreferenceQualifier() {
		return TclDebugPlugin.PLUGIN_ID;
	}

}
