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
package org.eclipse.dltk.tcl.internal.debug.ui.spawnpoints;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class TclSpawnpointPreferencePage extends
		AbstractConfigurationBlockPropertyAndPreferencePage {

	protected AbstractOptionsBlock createOptionsBlock(
			IStatusChangeListener newStatusChangedListener, IProject project,
			IWorkbenchPreferenceContainer container) {
		return new TclSpawnpointPreferenceBlock(newStatusChangedListener,
				project,  container);
	}

	protected String getHelpId() {
		return null;
	}

	protected String getProjectHelpId() {
		return null;
	}

	protected void setDescription() {
		// empty
	}

	protected void setPreferenceStore() {
		// empty
	}

	protected String getPreferencePageId() {
		return null;
	}

	protected String getPropertyPageId() {
		return null;
	}

}
