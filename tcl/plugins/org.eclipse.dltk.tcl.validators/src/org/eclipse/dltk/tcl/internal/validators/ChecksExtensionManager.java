/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/

package org.eclipse.dltk.tcl.internal.validators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.tcl.validators.ITclCheck;
import org.eclipse.dltk.tcl.validators.TclValidatorsCore;

/**
 * Load all definition extension points.
 */
public class ChecksExtensionManager {
	private static final String CATEGORY_ATTR = "category";
	private static final String COMMAND_ATTR = "command";
	private static final String CLASS_ATTR = "class";
	private static final String TITLE_ATTR = "title";
	private static final String ID_ATTR = "id";
	private static final String EXTENSION_ID = TclValidatorsCore.PLUGIN_ID
			+ ".tclCheck";
	private static ChecksExtensionManager sInstance;
	private List<TclCheckInfo> checks = new ArrayList<TclCheckInfo>();
	private boolean initialized = false;

	public static class TclCheckInfo {
		private String id;
		private String title;
		private ITclCheck check;
		private String commandName;
		private String category;

		public TclCheckInfo(ITclCheck check, String commandName, String id,
				String title, String category) {
			this.check = check;
			this.commandName = commandName;
			this.id = id;
			this.title = title;
			this.category = category;
		}

		public String getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public ITclCheck getCheck() {
			return check;
		}

		public String getCommandName() {
			return commandName;
		}

		public String getCategory() {
			return category;
		}
	}

	public ChecksExtensionManager() {
	}

	private void initialize() {
		if (initialized) {
			return;
		}
		initialized = true;
		IConfigurationElement[] configurationElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						EXTENSION_ID);
		for (IConfigurationElement config : configurationElements) {
			try {
				String id = config.getAttribute(ID_ATTR);
				String title = config.getAttribute(TITLE_ATTR);
				ITclCheck check = (ITclCheck) config
						.createExecutableExtension(CLASS_ATTR);
				String commandName = config.getAttribute(COMMAND_ATTR);
				String category = config.getAttribute(CATEGORY_ATTR);
				TclCheckInfo info = new TclCheckInfo(check, commandName, id,
						title, category);
				this.checks.add(info);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	public TclCheckInfo[] getChecks() {
		initialize();
		return this.checks.toArray(new TclCheckInfo[this.checks.size()]);
	}

	public static ChecksExtensionManager getInstance() {
		if (sInstance == null) {
			sInstance = new ChecksExtensionManager();
		}
		return sInstance;
	}
}
