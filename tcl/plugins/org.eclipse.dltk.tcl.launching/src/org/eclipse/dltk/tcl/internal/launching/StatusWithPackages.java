package org.eclipse.dltk.tcl.internal.launching;

import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.tcl.core.packages.TclInterpreterInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.tcl.launching.TclLaunchingPlugin;

/**
 * @since 1.1
 */
public class StatusWithPackages extends Status {

	private TclInterpreterInfo info;

	public StatusWithPackages(TclInterpreterInfo info) {
		super(OK, TclLaunchingPlugin.PLUGIN_ID, OK_STATUS.getMessage());
		this.info = info;
	}

	public TclInterpreterInfo getInterpreter() {
		return info;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(super.toString());
		buf.append("["); //$NON-NLS-1$
		for (TclPackageInfo pkg : info.getPackages()) {
			buf.append(" " + pkg.getName()); //$NON-NLS-1$
		}
		buf.append("]"); //$NON-NLS-1$
		return buf.toString();
	}
}
