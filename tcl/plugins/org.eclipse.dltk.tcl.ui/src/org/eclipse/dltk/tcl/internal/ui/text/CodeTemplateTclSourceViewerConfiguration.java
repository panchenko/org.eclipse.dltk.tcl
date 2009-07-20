/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.text;

import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.ui.text.TclPartitions;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.templates.TemplateVariableProcessor;
import org.eclipse.dltk.ui.text.templates.TemplateVariableTextHover;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.ITextEditor;

public class CodeTemplateTclSourceViewerConfiguration extends
		SimpleTclSourceViewerConfiguration {

	private final TemplateVariableProcessor fProcessor;

	public CodeTemplateTclSourceViewerConfiguration(IColorManager colorManager,
			IPreferenceStore store, ITextEditor editor,
			TemplateVariableProcessor processor) {
		super(colorManager, store, editor, TclPartitions.TCL_PARTITIONING,
				false);
		fProcessor = processor;
	}

	/*
	 * @see SourceViewerConfiguration#getContentAssistant(ISourceViewer)
	 */
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

		IPreferenceStore store = TclUI.getDefault().getPreferenceStore();
		ScriptTextTools textTools = TclUI.getDefault().getTextTools();
		IColorManager manager = textTools.getColorManager();

		ContentAssistant assistant = new ContentAssistant();
		assistant.setContentAssistProcessor(fProcessor,
				IDocument.DEFAULT_CONTENT_TYPE);
		// Register the same processor for strings and single line comments to
		// get code completion at the start of those partitions.
		assistant.setContentAssistProcessor(fProcessor,
				TclPartitions.TCL_STRING);
		assistant.setContentAssistProcessor(fProcessor,
				TclPartitions.TCL_COMMENT);

		assistant.enableAutoInsert(store
				.getBoolean(PreferenceConstants.CODEASSIST_AUTOINSERT));
		assistant.enableAutoActivation(store
				.getBoolean(PreferenceConstants.CODEASSIST_AUTOACTIVATION));
		assistant.setAutoActivationDelay(store
				.getInt(PreferenceConstants.CODEASSIST_AUTOACTIVATION_DELAY));
		assistant
				.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
		assistant
				.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		assistant
				.setInformationControlCreator(new IInformationControlCreator() {
					public IInformationControl createInformationControl(
							Shell parent) {
						return new DefaultInformationControl(parent, EditorsUI
								.getTooltipAffordanceString());
					}
				});

		Color background = getColor(store,
				PreferenceConstants.CODEASSIST_PARAMETERS_BACKGROUND, manager);
		assistant.setContextInformationPopupBackground(background);
		assistant.setContextSelectorBackground(background);

		Color foreground = getColor(store,
				PreferenceConstants.CODEASSIST_PARAMETERS_FOREGROUND, manager);
		assistant.setContextInformationPopupForeground(foreground);
		assistant.setContextSelectorForeground(foreground);

		return assistant;
	}

	private Color getColor(IPreferenceStore store, String key,
			IColorManager manager) {
		RGB rgb = PreferenceConverter.getColor(store, key);
		return manager.getColor(rgb);
	}

	/*
	 * @see SourceViewerConfiguration#getTextHover(ISourceViewer, String, int)
	 * 
	 * @since 2.1
	 */
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType, int stateMask) {
		return new TemplateVariableTextHover(fProcessor);
	}

}
