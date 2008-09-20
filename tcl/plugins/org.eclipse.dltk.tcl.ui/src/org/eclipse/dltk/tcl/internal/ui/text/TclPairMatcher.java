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
import org.eclipse.dltk.tcl.ast.expressions.TclBlockExpression;
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

	private final boolean DEBUG = false;

	private static final int MAX_PARSE_WAIT_TIME = 100;
	private static final int MIN_PARSE_INTERVAL = 2500;

	private final Object lock = new Object();

	private class ParserThread extends Thread {

		final String content;
		final long newTimestamp;
		final long newHashcode;
		final long startTime = System.currentTimeMillis();

		/**
		 * @param content
		 * @param newTimestamp
		 * @param newHashcode
		 */
		public ParserThread(String content, long newTimestamp, long newHashcode) {
			super(ParserThread.class.getName());
			this.content = content;
			this.newTimestamp = newTimestamp;
			this.newHashcode = newHashcode;
		}

		public void run() {
			try {
				if (DEBUG) {
					System.out.println("ParserThread - BEGIN"); //$NON-NLS-1$
				}
				final PairBlock[] pairs = computePairRanges(content);
				synchronized (lock) {
					cachedPairs = pairs;
					cachedHash = newHashcode;
					cachedStamp = newTimestamp;
					parsedAt = startTime;
				}
				if (DEBUG) {
					System.out
							.println("ParserThread - END " + (System.currentTimeMillis() - startTime)); //$NON-NLS-1$
				}
			} finally {
				synchronized (lock) {
					thread = null;
				}
			}
		}

	}

	private ParserThread thread = null;
	private long parsedAt = 0;

	private IDocument fDocument;

	private int fAnchor;

	private static class PairBlock {
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

	private long cachedHash = Long.MAX_VALUE;

	public TclPairMatcher() {
	}

	private static PairBlock[] computePairRanges(final String contents) {
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
					} else if (be instanceof TclBlockExpression) {
						int start = be.sourceStart();
						int end = be.sourceEnd();
						if (start >= 0 && start < end
								&& start < contents.length()
								&& end <= contents.length()
								&& contents.charAt(start) == '{'
								&& contents.charAt(end - 1) == '}') {
							result.add(new PairBlock(start, end - 1, '{'));
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
	private void recalc(final String content, long newTimestamp,
			long newHashcode) throws BadLocationException {
		final ParserThread t;
		synchronized (lock) {
			if (thread != null) {
				return;
			}
			thread = t = new ParserThread(content, newTimestamp, newHashcode);
		}
		t.start();
		try {
			t.join(MAX_PARSE_WAIT_TIME);
		} catch (InterruptedException e) {
			// ignore
		}
	}

	/**
	 * Recalcs pairs for the document, only if it is required
	 */
	private void updatePairs() throws BadLocationException {
		synchronized (lock) {
			if (System.currentTimeMillis() < parsedAt + MIN_PARSE_INTERVAL) {
				return;
			}
		}
		if (fDocument instanceof IDocumentExtension4) {
			final IDocumentExtension4 document = (IDocumentExtension4) fDocument;
			final long newTimestamp = document.getModificationStamp();
			synchronized (lock) {
				if (newTimestamp == cachedStamp) {
					return;
				}
			}
			recalc(fDocument.get(), newTimestamp, Long.MAX_VALUE);
		} else {
			final String content = fDocument.get();
			final int newHashCode = content.hashCode();
			synchronized (lock) {
				if (newHashCode == cachedHash) {
					return;
				}
			}
			recalc(content, -1, newHashCode);
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
			fDocument = document;

			if (!isBraceAt(document, offset)) {
				return null;
			}

			updatePairs();
			return matchPairsAt(offset);
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

	private IRegion matchPairsAt(int offset) {
		final PairBlock[] pairs;
		synchronized (lock) {
			pairs = cachedPairs;
		}
		if (pairs == null) {
			return null;
		}
		// TODO pairs should be sorted somehow...
		for (int i = 0, size = pairs.length; i < size; i++) {
			final PairBlock block = pairs[i];
			if (offset == block.end + 1) {
				fAnchor = LEFT;
				return new Region(block.start, 1);
			}
			if (offset == block.start + 1) {
				fAnchor = LEFT;
				return new Region(block.end, 1);
			}
		}
		return null;
	}
}
