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
package org.eclipse.dltk.tcl.internal.ui.editor;

import org.eclipse.dltk.internal.ui.editor.BracketInserter;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.ui.text.TclPartitions;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.texteditor.ITextEditorExtension3;

public class TclBracketInserter extends BracketInserter {

	TclBracketInserter(ScriptEditor scriptEditor) {
		super(scriptEditor);
	}

	private boolean isCloseStrings() {
		return TclUI.getDefault().getPreferenceStore().getBoolean(
				PreferenceConstants.EDITOR_CLOSE_STRINGS);
	}

	public void verifyKey(VerifyEvent event) {
		// early pruning to slow down normal typing as little as possible
		if (!event.doit
				|| editor.getInsertMode() != ITextEditorExtension3.SMART_INSERT)
			return;
		switch (event.character) {
		case '\"':
			break;
		default:
			return;
		}

		final ISourceViewer sourceViewer = this.editor.getScriptSourceViewer();
		IDocument document = sourceViewer.getDocument();

		final Point selection = sourceViewer.getSelectedRange();
		final int offset = selection.x;
		final int length = selection.y;

		try {
			final IRegion startLine = document
					.getLineInformationOfOffset(offset);
			final IRegion endLine = document.getLineInformationOfOffset(offset
					+ length);
			final char nextToken = offset + length < endLine.getOffset()
					+ endLine.getLength() ? document.getChar(offset + length)
					: 0;
			final char prevToken = offset > startLine.getOffset() ? document
					.getChar(offset - 1) : 0;

			// RubyHeuristicScanner scanner = new
			// RubyHeuristicScanner(document);
			// int nextToken = scanner.nextToken(offset + length, endLine
			// .getOffset()
			// + endLine.getLength());
			// String next = nextToken == ISymbols.TokenEOF ? null :
			// document.get(
			// offset, scanner.getPosition() - offset).trim();
			// int prevToken = scanner.previousToken(offset - 1, startLine
			// .getOffset());
			// int prevTokenOffset = scanner.getPosition();
			// if (prevTokenOffset < 0)
			// prevTokenOffset = 0;
			// String previous = offset > 1 && prevToken == ISymbols.TokenEOF ?
			// null
			// : document.get(prevTokenOffset, offset - prevTokenOffset)
			// .trim();
			switch (event.character) {
			case '"':
				if (!isCloseStrings()
						|| (nextToken != 0 && !Character
								.isWhitespace(nextToken))
						|| (prevToken != 0 && !Character
								.isWhitespace(prevToken)))
					return;
				break;

			default:
				return;
			}

			int correctedOffset = (document.getLength() > 0 && document
					.getLength() == offset) ? offset - 1 : offset;
			ITypedRegion partition = TextUtilities.getPartition(document,
					TclPartitions.TCL_PARTITIONING, correctedOffset, true);
			if (!IDocument.DEFAULT_CONTENT_TYPE.equals(partition.getType()))
				return;

			if (!this.editor.validateEditorInputState())
				return;

			insertBrackets(document, offset, length, event.character,
					getPeerCharacter(event.character));

			event.doit = false;

		} catch (BadLocationException e) {
			DLTKUIPlugin.log(e);
		} catch (BadPositionCategoryException e) {
			DLTKUIPlugin.log(e);
		}
	}

}
