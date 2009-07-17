package org.eclipse.dltk.tcl.internal.debug.ui.launchConfigurations;

import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @since 2.0
 */
public class TclEnvironmentTab extends EnvironmentTab {
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		Composite mainComposite = (Composite) getControl();
		Label l = new Label(mainComposite, SWT.WRAP);
		l
				.setText("TCLLIBPATH environment variable will be corrected in any way");
	}
}