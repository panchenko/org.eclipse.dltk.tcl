/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.tcl.ast.expressions.TclExecuteExpression;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.ast.TclAdvancedExecuteExpression;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ICharacterPairMatcher;

/**
 * Helper class for match pairs of characters.
 */
public final class TclPairMatcher implements ICharacterPairMatcher {

	private IDocument fDocument;

	private int fOffset;

	private int fStartPos;

	private int fEndPos;

	private int fAnchor;

	private class PairBlock {
		public PairBlock(int start, int end, char c) {
			this.start = start;
			this.end = end;
			this.c = c;
		}

		int start;

		int end;

		char c;
	};

	private PairBlock[] cachedPairs;

	private long cachedStamp = -1;

	private long cachedHash = -1;

	public TclPairMatcher() {
	}

	private PairBlock[] computePairRanges(String contents) {
		/*
		 * ISourceModule returned by editor.getInputModelElement() could be
		 * inconsistent with current editor contents so we always reparse.
		 */
		final ISourceParser pp = DLTKLanguageManager
				.getSourceParser(TclNature.NATURE_ID);
		final ModuleDeclaration md = pp.parse(null, contents.toCharArray(),
				null);
		if (md == null) {
			return new PairBlock[0];
		}
		final List result = new ArrayList();
		try {
			md.traverse(new ASTVisitor() {
				public boolean visitGeneral(ASTNode be) throws Exception {
					if (be instanceof StringLiteral) {
						result.add(new PairBlock(be.sourceStart(), be
								.sourceEnd() - 1, '\"'));
					} else if (be instanceof TclExecuteExpression) {
						result.add(new PairBlock(be.sourceStart(), be
								.sourceEnd() - 1, '['));
					} else if (be instanceof TclAdvancedExecuteExpression) {
						result.add(new PairBlock(be.sourceStart() - 1, be
								.sourceEnd(), '['));
					} else if (be instanceof Block) {
						int start = be.sourceStart();
						if (start != 0) {
							result.add(new PairBlock(start, be.sourceEnd() - 1,
									'{'));
						}
					}
					return super.visitGeneral(be);
				}
			});
		} catch (Exception e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}

		return (PairBlock[]) result.toArray(new PairBlock[result.size()]);
	}

	/**
	 * Fully recalcs pairs for document
	 * 
	 * @param doc
	 * @throws BadLocationException
	 */
	private void recalc(final String content) throws BadLocationException {
		cachedPairs = computePairRanges(content);

		if (fDocument instanceof IDocumentExtension4) {
			cachedStamp = ((IDocumentExtension4) fDocument)
					.getModificationStamp();
		} else {
			cachedHash = content.hashCode();
		}
	}

	/**
	 * Recalcs pairs for the document, only if it is required
	 */
	private void updatePairs() throws BadLocationException {
		if (fDocument instanceof IDocumentExtension4) {
			IDocumentExtension4 document = (IDocumentExtension4) fDocument;

			if (document.getModificationStamp() == cachedStamp) {
				return;
			}
			recalc(fDocument.get());

		} else {
			final String content = fDocument.get();

			if (content.hashCode() == cachedHash) {
				return;
			}
			recalc(content);
		}

	}

	private static boolean isBrace(char c) {
		return (c == '{' || c == '}' || c == '\"' || c == '[' || c == ']');
	}

	/**
	 * Tests that either the symbol at <code>offset</code> or the previous one
	 * is a brace. This function checks that offsets are in the allowed range.
	 * 
	 * @param document
	 * @param offset
	 * @return
	 * @throws BadLocationException
	 */
	private static boolean isBraceAt(IDocument document, int offset)
			throws BadLocationException {
		// test symbol at offset
		if (offset < document.getLength() && isBrace(document.getChar(offset))) {
			return true;
		}
		// test previous symbol
		if (offset > 0 && isBrace(document.getChar(offset - 1))) {
			return true;
		}
		return false;
	}

	public IRegion match(IDocument document, int offset) {
		if (document == null || offset < 0) {
			throw new IllegalArgumentException();
		}

		try {
			fOffset = offset;
			fDocument = document;

			if (!isBraceAt(document, offset)) {
				return null;
			}

			updatePairs();

			if (matchPairsAt() && fStartPos != fEndPos)
				return new Region(fStartPos, fEndPos - fStartPos + 1);
		} catch (BadLocationException e) {
			if (DLTKCore.DEBUG_PARSER)
				e.printStackTrace();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.source.ICharacterPairMatcher#getAnchor()
	 */
	public int getAnchor() {
		return fAnchor;
	}

	public void dispose() {
		clear();
		fDocument = null;
	}

	public void clear() {
	}

	private boolean matchPairsAt() {

		fStartPos = -1;
		fEndPos = -1;

		for (int i = 0; i < cachedPairs.length; i++) {
			PairBlock block = cachedPairs[i];

			if (fOffset == block.end + 1) {
				fStartPos = block.start - 1;
				fEndPos = block.start;
				fAnchor = LEFT;
				return true;
			}
			if (fOffset == block.start + 1) {
				fStartPos = block.end - 1;
				fEndPos = block.end;
				fAnchor = LEFT;
				return true;
			}

		}

		return false;
	}
}
