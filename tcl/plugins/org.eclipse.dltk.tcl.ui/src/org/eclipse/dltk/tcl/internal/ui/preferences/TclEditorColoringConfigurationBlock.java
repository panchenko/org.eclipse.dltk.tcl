/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.preferences;

import java.io.InputStream;

import org.eclipse.dltk.internal.ui.editor.ScriptSourceViewer;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.internal.ui.editor.TclDocumentSetupParticipant;
import org.eclipse.dltk.tcl.internal.ui.text.SimpleTclSourceViewerConfiguration;
import org.eclipse.dltk.tcl.internal.ui.text.TclTextTools;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.tcl.ui.text.TclPartitions;
import org.eclipse.dltk.ui.preferences.AbstractScriptEditorColoringConfigurationBlock;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.preferences.PreferencesMessages;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.texteditor.ITextEditor;

public class TclEditorColoringConfigurationBlock extends
		AbstractScriptEditorColoringConfigurationBlock implements
		IPreferenceConfigurationBlock {

	private static final String PREVIEW_FILE_NAME = "PreviewFile.txt";

	private static final String[][] fSyntaxColorListModel = new String[][] {
			{ PreferencesMessages.DLTKEditorPreferencePage_singleLineComment,
					TclPreferenceConstants.EDITOR_SINGLE_LINE_COMMENT_COLOR,
					sCommentsCategory },
			{ PreferencesMessages.DLTKEditorPreferencePage_CommentTaskTags,
					TclPreferenceConstants.COMMENT_TASK_TAGS, sCommentsCategory },
			{ PreferencesMessages.DLTKEditorPreferencePage_keywords,
					TclPreferenceConstants.EDITOR_KEYWORD_COLOR, sCoreCategory },

			{ PreferencesMessages.DLTKEditorPreferencePage_returnKeyword,
					TclPreferenceConstants.EDITOR_KEYWORD_RETURN_COLOR,
					sCoreCategory },

			{ PreferencesMessages.DLTKEditorPreferencePage_strings,
					TclPreferenceConstants.EDITOR_STRING_COLOR, sCoreCategory },

			{ PreferencesMessages.DLTKEditorPreferencePage_numbers,
					TclPreferenceConstants.EDITOR_NUMBER_COLOR, sCoreCategory },
			{ PreferencesMessages.DLTKEditorPreferencePage_variables,
					TclPreferenceConstants.EDITOR_VARIABLE_COLOR, sCoreCategory },
/*					
			{ PreferencesMessages.DLTKEditorPreferencePage_function_colors,
				TclPreferenceConstants.EDITOR_PROCEDURES_COLOR, sCoreCategory },
					
			{ PreferencesMessages.DLTKEditorPreferencePage_class_colors,
					TclPreferenceConstants.EDITOR_CLASSES_COLOR, sCoreCategory },
			{ PreferencesMessages.DLTKEditorPreferencePage_arguments,
				TclPreferenceConstants.EDITOR_ARGUMENTS_COLOR, sCoreCategory },
					
			{ PreferencesMessages.DLTKEditorPreferencePage_base_classes,
				TclPreferenceConstants.EDITOR_BASE_CLASS_COLOR, sCoreCategory },
*/
	};

	public TclEditorColoringConfigurationBlock(OverlayPreferenceStore store) {
		super(store);
	}

	protected String[][] getSyntaxColorListModel() {
		return fSyntaxColorListModel;
	}

	protected ProjectionViewer createPreviewViewer(Composite parent,
			IVerticalRuler verticalRuler, IOverviewRuler overviewRuler,
			boolean showAnnotationsOverview, int styles, IPreferenceStore store) {
		return new ScriptSourceViewer(parent, verticalRuler, overviewRuler,
				showAnnotationsOverview, styles, store);
	}

	protected ScriptSourceViewerConfiguration createSimpleSourceViewerConfiguration(
			IColorManager colorManager, IPreferenceStore preferenceStore,
			ITextEditor editor, boolean configureFormatter) {
		return new SimpleTclSourceViewerConfiguration(colorManager,
				preferenceStore, editor, TclPartitions.TCL_PARTITIONING,
				configureFormatter);
	}

	protected void setDocumentPartitioning(IDocument document) {
		TclDocumentSetupParticipant participant = new TclDocumentSetupParticipant();
		participant.setup(document);
	}

	protected InputStream getPreviewContentReader() {
		return getClass().getResourceAsStream(PREVIEW_FILE_NAME);
	}
	
	/**
	 * Override getTextTools() for enabling semantic highlighting in preview editor 
	 */
	protected ScriptTextTools getTextTools() {
		return TclUI.getDefault().getTextTools();
	}
	
}
