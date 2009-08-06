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
package org.eclipse.dltk.tcl.internal.console.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.core.environment.EnvironmentChangedListener;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.internal.launching.InterpreterListener;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallChangedListener;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * @since 2.0
 */
public class OpenConsoleAction extends AbstractPulldownAction {

	private final EnvironmentChangedListener environmentListener = new EnvironmentChangedListener() {
		@Override
		public void environmentsModified() {
			fRecreateMenu = true;
		}
	};

	private final IInterpreterInstallChangedListener interpreterListener = new InterpreterListener() {
		@Override
		protected void changed() {
			fRecreateMenu = true;
		}
	};

	@Override
	public void init(IWorkbenchWindow window) {
		super.init(window);
		EnvironmentManager.addEnvironmentChangedListener(environmentListener);
		ScriptRuntime.addInterpreterInstallChangedListener(interpreterListener);
	}

	@Override
	public void dispose() {
		ScriptRuntime
				.removeInterpreterInstallChangedListener(interpreterListener);
		EnvironmentManager
				.removeEnvironmentChangedListener(environmentListener);
		super.dispose();
	}

	private final class OpenInstallConsoleAction extends Action {
		private final IInterpreterInstall install;

		/**
		 * @param text
		 * @param install
		 * @param menu
		 */
		private OpenInstallConsoleAction(IInterpreterInstall install) {
			super(NLS.bind("{0} ({1})", install.getName(), install
					.getInstallLocation().toOSString()));
			this.install = install;
		}

		@Override
		public void run() {
			new TclConsoleFactory().openConsole(install, NLS.bind("{0} - {1}",
					install.getEnvironment().getName(), install
							.getInstallLocation().toOSString()));
		}
	}

	private static class EnvironmentEntry {
		final IEnvironment environment;
		final List<IInterpreterInstall> installs = new ArrayList<IInterpreterInstall>();

		public EnvironmentEntry(IEnvironment environment) {
			this.environment = environment;
		}

	}

	@Override
	protected void fillMenu(final Menu menu) {
		// collect environments
		final Map<String, EnvironmentEntry> environments = new HashMap<String, EnvironmentEntry>();
		for (IEnvironment environment : EnvironmentManager.getEnvironments()) {
			environments.put(environment.getId(), new EnvironmentEntry(
					environment));
		}
		// collect interpreters
		for (IInterpreterInstallType type : ScriptRuntime
				.getInterpreterInstallTypes(TclNature.NATURE_ID)) {
			IInterpreterInstall[] installs = type.getInterpreterInstalls();
			for (IInterpreterInstall install : installs) {
				final EnvironmentEntry entry = environments.get(install
						.getEnvironmentId());
				if (entry != null) {
					entry.installs.add(install);
				}
			}
		}
		// copy to list
		final List<EnvironmentEntry> list = new ArrayList<EnvironmentEntry>(
				environments.values());
		// remove hosts without interpreters
		for (Iterator<EnvironmentEntry> i = list.iterator(); i.hasNext();) {
			final EnvironmentEntry entry = i.next();
			if (entry.installs.isEmpty()) {
				i.remove();
			}
		}
		// sort
		Collections.sort(list, new Comparator<EnvironmentEntry>() {
			public int compare(EnvironmentEntry o1, EnvironmentEntry o2) {
				if (o1.environment.isLocal() != o2.environment.isLocal()) {
					return o1.environment.isLocal() ? -1 : +1;
				}
				return o1.environment.getName().compareToIgnoreCase(
						o2.environment.getName());
			}
		});
		for (EnvironmentEntry entry : list) {
			// final IInterpreterInstall defaultInstall = ScriptRuntime
			// .getDefaultInterpreterInstall(new DefaultInterpreterEntry(
			// TclNature.NATURE_ID, entry.environment.getId()));
			final Menu eMenu = addSubmenu(menu, entry.environment.getName());
			Collections.sort(entry.installs,
					new Comparator<IInterpreterInstall>() {
						public int compare(IInterpreterInstall o1,
								IInterpreterInstall o2) {
							return o1.getName().compareTo(o2.getName());
						}
					});
			for (final IInterpreterInstall install : entry.installs) {
				addToMenu(eMenu, new OpenInstallConsoleAction(install));
			}
		}
	}

	@Override
	public void run(IAction action) {
		new TclConsoleFactory().openConsole();
	}
}
