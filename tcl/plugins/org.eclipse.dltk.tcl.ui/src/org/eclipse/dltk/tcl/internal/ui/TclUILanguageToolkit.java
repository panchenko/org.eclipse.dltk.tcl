/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.tcl.core.TclConstants;
import org.eclipse.dltk.tcl.core.TclLanguageToolkit;
import org.eclipse.dltk.tcl.internal.ui.text.SimpleTclSourceViewerConfiguration;
import org.eclipse.dltk.tcl.internal.ui.text.TclCorrectionProcessor;
import org.eclipse.dltk.ui.IDLTKCorrectionProcessor;
import org.eclipse.dltk.ui.IDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.ScriptElementLabels;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.viewsupport.ScriptUILabelProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.Annotation;

public class TclUILanguageToolkit implements IDLTKUILanguageToolkit,
		IDLTKCorrectionProcessor {

	private static TclUILanguageToolkit sToolkit = null;

	public static IDLTKUILanguageToolkit getInstance() {
		if (sToolkit == null) {
			sToolkit = new TclUILanguageToolkit();
		}
		return sToolkit;
	}

	private static class TclScriptElementLabels extends ScriptElementLabels {
		public void getElementLabel(IModelElement element, long flags,
				StringBuffer buf) {
			StringBuffer buffer = new StringBuffer(60);
			super.getElementLabel(element, flags, buffer);
			String s = buffer.toString();
			if (s != null && !s.startsWith(element.getElementName())) {
				if (s.indexOf('$') != -1) {
					s = s.replaceAll("\\$", "::"); //$NON-NLS-1$//$NON-NLS-2$
				}
			}
			buf.append(s);
		}

		protected char getTypeDelimiter() {
			return '$';
		}
		// protected void getTypeLabel(IType type, long flags, StringBuffer buf)
		// {
		// StringBuffer buffer = new StringBuffer(60);
		// super.getTypeLabel(type, flags, buffer);
		// if( type.getParent() instanceof ISourceModule ) {
		// buf.append("$");
		// }
		// buf.append(buffer);
		// }
	};

	private static TclScriptElementLabels sInstance = new TclScriptElementLabels();

	public ScriptElementLabels getScriptElementLabels() {
		return sInstance;
	}

	public IPreferenceStore getPreferenceStore() {
		return TclUI.getDefault().getPreferenceStore();
	}

	public IDLTKLanguageToolkit getCoreToolkit() {
		return TclLanguageToolkit.getDefault();
	}

	public IDialogSettings getDialogSettings() {
		return TclUI.getDefault().getDialogSettings();
	}

	public String getPartitioningId() {
		return TclConstants.TCL_PARTITIONING;
	}

	public String getEditorId(Object inputElement) {
		return "org.eclipse.dltk.tcl.ui.editor.TclEditor"; //$NON-NLS-1$
	}

	public String getInterpreterContainerId() {
		return "org.eclipse.dltk.tcl.launching.INTERPRETER_CONTAINER"; //$NON-NLS-1$
	}

	public ScriptUILabelProvider createScriptUILabelProvider() {
		return null;
	}

	public boolean getProvideMembers(ISourceModule element) {
		return true;
	}

	public ScriptTextTools getTextTools() {
		return TclUI.getDefault().internalgetTextTools();
	}

	public ScriptSourceViewerConfiguration createSourceViewerConfiguration() {
		return new SimpleTclSourceViewerConfiguration(getTextTools()
				.getColorManager(), getPreferenceStore(), null,
				getPartitioningId(), false);
	}

	private static final String INTERPRETERS_PREFERENCE_PAGE_ID = "org.eclipse.dltk.tcl.preferences.interpreters"; //$NON-NLS-1$
	private static final String DEBUG_PREFERENCE_PAGE_ID = "org.eclipse.dltk.tcl.preferences.debug"; //$NON-NLS-1$
	private static final String[] EDITOR_PREFERENCE_PAGES_IDS = {
			"org.eclipse.dltk.tcl.preferences.editor", //$NON-NLS-1$
			"org.eclipse.dltk.tcl.preferences.templates", //$NON-NLS-1$
			"org.eclipse.dltk.tcl.preferences.editor.syntaxcoloring", //$NON-NLS-1$
			"org.eclipse.dltk.tcl.preferences.editor.hovers", //$NON-NLS-1$
			"org.eclipse.dltk.tcl.preferences.editor.smarttyping", //$NON-NLS-1$
			"org.eclipse.dltk.tcl.preferences.editor.folding", //$NON-NLS-1$
			"org.eclipse.dltk.tcl.ui.assistance" //$NON-NLS-1$
	};

	public String getInterpreterPreferencePage() {
		return INTERPRETERS_PREFERENCE_PAGE_ID;
	}

	public String getDebugPreferencePage() {
		return DEBUG_PREFERENCE_PAGE_ID;
	}

	public String[] getEditorPreferencePages() {
		return EDITOR_PREFERENCE_PAGES_IDS;
	}

	public boolean hasCorrections(Annotation annotation) {
		return TclCorrectionProcessor.hasCorrections(annotation);
	}
}
