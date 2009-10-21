package org.eclipse.dltk.tcl.internal.launching;

import java.util.List;

import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.tcl.launching.TclLaunchingPlugin;

/**
 * @since 1.1
 */
public class StatusWithPackages extends Status {

	private List<String> packages;

	public StatusWithPackages(List<String> packages) {
		super(OK, TclLaunchingPlugin.PLUGIN_ID, OK_STATUS.getMessage());
		this.packages = packages;
	}

	public List<String> getPackages() {
		return packages;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(super.toString());
		buf.append("["); //$NON-NLS-1$
		for (String pkg : packages) {
			buf.append(" " + pkg); //$NON-NLS-1$
		}
		buf.append("]"); //$NON-NLS-1$
		return buf.toString();
	}
}
