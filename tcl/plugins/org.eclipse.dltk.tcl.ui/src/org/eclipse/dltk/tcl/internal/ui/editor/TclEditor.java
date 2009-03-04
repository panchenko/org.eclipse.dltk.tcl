/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.editor;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.internal.ui.actions.FoldingActionGroup;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.internal.ui.editor.ScriptOutlinePage;
import org.eclipse.dltk.tcl.core.TclLanguageToolkit;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.internal.ui.text.TclPairMatcher;
import org.eclipse.dltk.tcl.internal.ui.text.folding.TclFoldingStructureProvider;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.tcl.ui.text.TclPartitions;
import org.eclipse.dltk.ui.actions.GenerateActionGroup;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.folding.IFoldingStructureProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

public class TclEditor extends ScriptEditor {
	public static final String EDITOR_ID = "org.eclipse.dltk.tcl.ui.editor.TclEditor"; //$NON-NLS-1$

	public static final String EDITOR_CONTEXT = "#TclEditorContext"; //$NON-NLS-1$

	public static final String RULER_CONTEXT = "#TclRulerContext"; //$NON-NLS-1$

	private IFoldingStructureProvider foldingProvider;

	private TclPairMatcher bracketMatcher;

	protected void initializeEditor() {
		super.initializeEditor();

		setEditorContextMenuId(EDITOR_CONTEXT);
		setRulerContextMenuId(RULER_CONTEXT);
	}

	protected void createActions() {
		super.createActions();
		ActionGroup generateActions = new GenerateActionGroup(this,
				ITextEditorActionConstants.GROUP_EDIT);
		fActionGroups.addGroup(generateActions);
		fContextMenuGroup.addGroup(generateActions);
	}

	final static String[] properties = new String[] {
			TclPreferenceConstants.EDITOR_FOLDING_BLOCKS,
			TclPreferenceConstants.EDITOR_FOLDING_EXCLUDE_LIST,
			TclPreferenceConstants.EDITOR_FOLDING_INCLUDE_LIST, };

	protected String[] getFoldingEventPreferenceKeys() {
		return properties;
	}

	protected IPreferenceStore getScriptPreferenceStore() {
		return TclUI.getDefault().getPreferenceStore();
	}

	public ScriptTextTools getTextTools() {
		return TclUI.getDefault().getTextTools();
	}

	protected ScriptOutlinePage doCreateOutlinePage() {
		return new TclOutlinePage(this, TclUI.getDefault().getPreferenceStore());
	}

	protected void connectPartitioningToElement(IEditorInput input,
			IDocument document) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 doc = (IDocumentExtension3) document;
			if (doc.getDocumentPartitioner(TclPartitions.TCL_PARTITIONING) == null) {
				IDocumentSetupParticipant participant = new TclDocumentSetupParticipant();
				participant.setup(document);
			}
		}
	}

	protected IFoldingStructureProvider getFoldingStructureProvider() {
		if (foldingProvider == null) {
			foldingProvider = new TclFoldingStructureProvider();
		}

		return foldingProvider;
	}

	protected FoldingActionGroup createFoldingActionGroup() {
		return new FoldingActionGroup(this, getViewer(), TclUI.getDefault()
				.getPreferenceStore());
	}

	public String getEditorId() {
		return EDITOR_ID;
	}

	protected void initializeKeyBindingScopes() {
		setKeyBindingScopes(new String[] { "org.eclipse.dltk.ui.tclEditorScope" }); //$NON-NLS-1$
	}

	public IDLTKLanguageToolkit getLanguageToolkit() {
		return TclLanguageToolkit.getDefault();
	}

	public String getCallHierarchyID() {
		return "org.eclipse.dltk.callhierarchy.view"; //$NON-NLS-1$
	}

	/**
	 * Jumps to the matching bracket.
	 */
	public void gotoMatchingBracket() {
		ISourceViewer sourceViewer = getSourceViewer();
		IDocument document = sourceViewer.getDocument();
		if (document == null)
			return;

		IRegion selection = getSignedSelection(sourceViewer);

		int selectionLength = Math.abs(selection.getLength());
		if (selectionLength > 1) {
			setStatusLineErrorMessage(ActionMessages.TclEditor_NoBracketSelected);
			sourceViewer.getTextWidget().getDisplay().beep();
			return;
		}

		// #26314
		int sourceCaretOffset = selection.getOffset() + selection.getLength();
		if (isSurroundedByBrackets(document, sourceCaretOffset))
			sourceCaretOffset -= selection.getLength();

		IRegion region = bracketMatcher.match(document, sourceCaretOffset);
		if (region == null) {
			setStatusLineErrorMessage(ActionMessages.TclEditor_NoMatchongBracket);
			sourceViewer.getTextWidget().getDisplay().beep();
			return;
		}

		int offset = region.getOffset();
		int length = region.getLength();

		if (length < 1)
			return;

		int anchor = bracketMatcher.getAnchor();
		// http://dev.eclipse.org/bugs/show_bug.cgi?id=34195
		int targetOffset = (ICharacterPairMatcher.RIGHT == anchor) ? offset + 1
				: offset + length;

		boolean visible = false;
		if (sourceViewer instanceof ITextViewerExtension5) {
			ITextViewerExtension5 extension = (ITextViewerExtension5) sourceViewer;
			visible = (extension.modelOffset2WidgetOffset(targetOffset) > -1);
		} else {
			IRegion visibleRegion = sourceViewer.getVisibleRegion();
			// http://dev.eclipse.org/bugs/show_bug.cgi?id=34195
			visible = (targetOffset >= visibleRegion.getOffset() && targetOffset <= visibleRegion
					.getOffset()
					+ visibleRegion.getLength());
		}

		if (!visible) {
			setStatusLineErrorMessage(ActionMessages.TclEditor_MatchingBracketOutsideSelectedElement);
			sourceViewer.getTextWidget().getDisplay().beep();
			return;
		}

		if (selection.getLength() < 0)
			targetOffset -= selection.getLength();

		sourceViewer.setSelectedRange(targetOffset, selection.getLength());
		sourceViewer.revealRange(targetOffset, selection.getLength());
	}

	protected void configureSourceViewerDecorationSupport(
			SourceViewerDecorationSupport support) {
		bracketMatcher = new TclPairMatcher();
		support.setCharacterPairMatcher(bracketMatcher);
		support.setMatchingCharacterPainterPreferenceKeys(MATCHING_BRACKETS,
				MATCHING_BRACKETS_COLOR);

		super.configureSourceViewerDecorationSupport(support);
	}
}
