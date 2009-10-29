/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *     xored software, Inc. - TCL ManPageFolder management refactoring (Alex Panchenko <alex@xored.com>)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.console.ui.ScriptConsoleUIPlugin;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.ui.text.TclCodeTemplateAccess;
import org.eclipse.dltk.tcl.internal.ui.text.TclTextTools;
import org.eclipse.dltk.ui.DLTKUILanguageManager;
import org.eclipse.dltk.ui.IDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.text.templates.ICodeTemplateAccess;
import org.eclipse.dltk.ui.text.templates.ITemplateAccess;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class TclUI extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "org.eclipse.dltk.tcl.ui"; //$NON-NLS-1$
	public static final String ID_ACTION_SET = "org.eclipse.dltk.tcl.ui.TclActionSet"; //$NON-NLS-1$

	// The shared instance.
	private static TclUI plugin;

	private TclTextTools fTclTextTools;

	/**
	 * The constructor.
	 */
	public TclUI() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		ScriptConsoleUIPlugin.getDefault();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		try {
			if (codeTemplateAccess != null) {
				if (codeTemplateAccess instanceof ITemplateAccess.ITemplateAccessInternal) {
					((ITemplateAccess.ITemplateAccessInternal) codeTemplateAccess)
							.dispose();
				}
				codeTemplateAccess = null;
			}
		} finally {
			super.stop(context);
			plugin = null;
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static TclUI getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public synchronized TclTextTools getTextTools() {
		IDLTKUILanguageToolkit languageToolkit = DLTKUILanguageManager
				.getLanguageToolkit(TclNature.NATURE_ID);
		return (TclTextTools) languageToolkit.getTextTools();
	}

	public synchronized TclTextTools internalgetTextTools() {
		if (fTclTextTools == null)
			fTclTextTools = new TclTextTools(true);
		return fTclTextTools;
	}

	public static void error(String message) {
		plugin.getLog()
				.log(
						new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK,
								message, null));
	}

	public static void error(Throwable t) {
		error(t.getMessage(), t);
	}

	public static void error(String message, Throwable t) {
		plugin.getLog().log(
				new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, t));
	}

	private ICodeTemplateAccess codeTemplateAccess = null;

	public ICodeTemplateAccess getCodeTemplateAccess() {
		if (codeTemplateAccess == null) {
			codeTemplateAccess = new TclCodeTemplateAccess();
		}
		return codeTemplateAccess;
	}

}
