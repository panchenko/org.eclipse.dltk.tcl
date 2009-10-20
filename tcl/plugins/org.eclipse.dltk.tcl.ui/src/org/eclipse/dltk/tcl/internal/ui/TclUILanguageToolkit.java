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
import org.eclipse.dltk.core.IImportContainer;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.internal.core.ImportContainer;
import org.eclipse.dltk.tcl.core.TclConstants;
import org.eclipse.dltk.tcl.core.TclLanguageToolkit;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageElement;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageFragment;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageSourceModule;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesElement;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesFragment;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesSourceModule;
import org.eclipse.dltk.tcl.internal.ui.text.SimpleTclSourceViewerConfiguration;
import org.eclipse.dltk.ui.AbstractDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.IDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.ScriptElementLabels;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.viewsupport.ScriptUILabelProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;

public class TclUILanguageToolkit extends AbstractDLTKUILanguageToolkit {

	private static TclUILanguageToolkit sToolkit = null;

	public static synchronized IDLTKUILanguageToolkit getInstance() {
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

		@Override
		protected void getImportContainerLabel(IModelElement element,
				long flags, StringBuffer buf) {
			IImportContainer container = (ImportContainer) element;
			if (TclConstants.SOURCE_CONTAINER.equals(container
					.getContainerName())) {
				buf.append("sourced files");
			} else if (TclConstants.REQUIRE_CONTAINER.equals(container
					.getContainerName())) {
				buf.append("required packages");
			} else {
				super.getImportContainerLabel(element, flags, buf);
			}
		}

		protected char getTypeDelimiter() {
			return '$';
		}

		public void getScriptFolderLabel(IProjectFragment pack, long flags,
				StringBuffer buf) {
			if (pack instanceof TclPackageFragment
					|| pack instanceof TclSourcesFragment) {
				// buf.append(((IProjectFragment)
				// pack).getPath().lastSegment());
				// buf.append(' ');
				return;
			}
			super.getScriptFolderLabel(pack, flags, buf);
		}

		public void getProjectFragmentLabel(IProjectFragment root, long flags,
				StringBuffer buf) {
			if (root instanceof TclPackageFragment) {
				buf.append(((TclPackageFragment) root).getPackageName());
				buf.append(' ');
				return;
			}
			// if (root instanceof TclSourcesFragment) {
			// return;
			// }
			super.getProjectFragmentLabel(root, flags, buf);
		};

		protected void getScriptFolderLabel(IScriptFolder folder, long flags,
				StringBuffer buf) {
			// if( folder instanceof PodSourcesFolder ) {
			// return;
			// }
			// boolean podFolder = true;// folder instanceof PodSourcesFolder;
			if (getFlag(flags, P_QUALIFIED)) {
				getProjectFragmentLabel((IProjectFragment) folder.getParent(),
						ROOT_QUALIFIED, buf);
				buf.append('/');
			}
			// refreshPackageNamePattern();
			if (folder.isRootFolder()) {
				// if (!podFolder) {
				// buf.append(DEFAULT_PACKAGE);
				// }
			} else if (getFlag(flags, P_COMPRESSED) && fgPkgNameLength >= 0) {
				String name = folder.getElementName();
				int start = 0;
				int dot = name.indexOf(IScriptFolder.PACKAGE_DELIMITER, start);
				while (dot > 0) {
					if (dot - start > fgPkgNameLength - 1) {
						buf.append(fgPkgNamePrefix);
						if (fgPkgNameChars > 0)
							buf.append(name.substring(start, Math.min(start
									+ fgPkgNameChars, dot)));
						buf.append(fgPkgNamePostfix);
					} else
						buf.append(name.substring(start, dot + 1));
					start = dot + 1;
					dot = name.indexOf(IScriptFolder.PACKAGE_DELIMITER, start);
				}
				buf.append(name.substring(start));
			} else {
				getScriptFolderLabel(folder, buf);
			}
			if (getFlag(flags, P_POST_QUALIFIED)) {
				// if (!podFolder) {
				// buf.append(CONCAT_STRING);
				// }
				getProjectFragmentLabel((IProjectFragment) folder.getParent(),
						ROOT_QUALIFIED, buf);
			}
		}

		protected void getScriptFolderLabel(IScriptFolder folder,
				StringBuffer buf) {
			if (folder instanceof TclPackageElement
					|| folder instanceof TclSourcesElement) {
				return;
			}
			String name = folder.getElementName();
			// name = name.replace(IScriptFolder.PACKAGE_DELIMITER, '.');
			buf.append(name);
		}

		protected void getSourceModule(ISourceModule module, long flags,
				StringBuffer buf) {
			if (getFlag(flags, CU_QUALIFIED)) {
				IScriptFolder pack = (IScriptFolder) module.getParent();

				getScriptFolderLabel(pack, (flags & QUALIFIER_FLAGS), buf);
				// if (!(module instanceof PodModule)) {
				// buf.append(getTypeDelimiter(module));
				// }
			}
			buf.append(module.getElementName());

			if (getFlag(flags, CU_POST_QUALIFIED)) {
				if ((!(module instanceof TclPackageSourceModule || module instanceof TclSourcesSourceModule))) {
					buf.append(CONCAT_STRING);
				}
				getScriptFolderLabel((IScriptFolder) module.getParent(), flags
						& QUALIFIER_FLAGS, buf);
			}
		};
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

}
