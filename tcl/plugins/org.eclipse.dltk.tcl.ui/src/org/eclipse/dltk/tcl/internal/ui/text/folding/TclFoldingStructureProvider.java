/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.text.folding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.ILog;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.tcl.ast.ITclStatementLookLike;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.ast.expressions.TclBlockExpression;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.ast.IfStatement;
import org.eclipse.dltk.tcl.core.ast.TclCatchStatement;
import org.eclipse.dltk.tcl.core.ast.TclForStatement;
import org.eclipse.dltk.tcl.core.ast.TclForeachStatement;
import org.eclipse.dltk.tcl.core.ast.TclSwitchStatement;
import org.eclipse.dltk.tcl.core.ast.TclWhileStatement;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.internal.ui.text.TclPartitionScanner;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.tcl.ui.text.TclPartitions;
import org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider;
import org.eclipse.dltk.ui.text.folding.IElementCommentResolver;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

/**
 */
public class TclFoldingStructureProvider extends
		AbstractASTFoldingStructureProvider {

	/* preferences */
	private int fBlockFolding = 0;
	private List fBlockIncludeList = new ArrayList();
	private List fBlockExcludeList = new ArrayList();

	private boolean fInitCollapseOtherBlocks;

	@Override
	public IElementCommentResolver createElementCommentResolver(
			IModelElement modelElement, String contents) {
		return new TclElementCommentResolver((ISourceModule) modelElement,
				contents);
	}

	@Override
	protected CodeBlock[] getCodeBlocks(String code, int offset) {
		/*
		 * if an ASTVisitor implementation is created for this, just override
		 * getFoldingVisitor() and remove this method
		 */
		ModuleDeclaration md = parse(code, offset);
		List statements = md.getStatements();
		if (statements == null) {
			return new CodeBlock[0];
		}

		List result = new ArrayList();
		traverse(result, statements, offset, code);

		return (CodeBlock[]) result.toArray(new CodeBlock[result.size()]);
	}

	private void checkStatement(String code, int offset, List result,
			Statement sst) {
		if (sst instanceof TclStatement) {
			TclStatement statement = (TclStatement) sst;
			result.add(new CodeBlock(statement, new Region(offset
					+ statement.sourceStart(), statement.sourceEnd()
					- statement.sourceStart())));

			Iterator si = statement.getExpressions().iterator();
			while (si.hasNext()) {
				Expression ex = (Expression) si.next();
				if (ex instanceof TclBlockExpression) {
					TclBlockExpression be = (TclBlockExpression) ex;
					try {
						String newContents = code.substring(
								be.sourceStart() + 1, be.sourceEnd() - 1);
						CodeBlock[] cb = getCodeBlocks(newContents, offset
								+ be.sourceStart() + 1);
						for (int j = 0; j < cb.length; j++) {
							result.add(cb[j]);
						}
					} catch (StringIndexOutOfBoundsException e) {
					}
				}
			}
		}
	}

	private class TclFoldBlock extends Block {
		public TclFoldBlock(Block block) {
			super(block.sourceStart(), block.sourceEnd());
			this.getStatements().addAll(block.getStatements());
		}
	}

	private void traverse(List result, List statements, int offset, String code) {

		for (Iterator iterator = statements.iterator(); iterator.hasNext();) {
			ASTNode node = (ASTNode) iterator.next();
			if (node instanceof TclStatement) {
				checkStatement(code, offset, result, (Statement) node);
				continue;
			}
			boolean fold = false;
			List children = null;
			if (node instanceof TypeDeclaration) {
				TypeDeclaration statement = (TypeDeclaration) node;
				children = statement.getStatements();
				fold = true;
			} else if (node instanceof MethodDeclaration) {
				MethodDeclaration statement = (MethodDeclaration) node;
				children = statement.getStatements();
				fold = true;
			} else if (node instanceof IfStatement) {
				fold = true;
				children = new ArrayList();
				IfStatement statement = (IfStatement) node;
				Statement thenNode = statement.getThen();
				if (thenNode instanceof Block) {
					children.addAll(((Block) thenNode).getStatements());
				}
				ASTNode elseNode = statement.getElse();
				if (elseNode instanceof Block) {
					children.addAll(((Block) elseNode).getStatements());
				}
			} else if (node instanceof TclCatchStatement) {
				fold = true;
				TclCatchStatement statement = (TclCatchStatement) node;
				children = statement.getStatements();
			} else if (node instanceof TclSwitchStatement) {
				fold = true;
				TclSwitchStatement statement = (TclSwitchStatement) node;
				Block alts = statement.getAlternatives();
				if (alts != null) {
					List childs = alts.getStatements();
					children = new ArrayList();
					for (int i = 0; i < childs.size(); i++) {
						ASTNode child = (ASTNode) childs.get(i);
						if (child instanceof Block) {
							result.add(new CodeBlock(new TclFoldBlock(
									(Block) child), new Region(offset
									+ child.sourceStart(), child.sourceEnd()
									- child.sourceStart())));
							children.addAll(((Block) child).getStatements());
						}
					}
				}
			} else if (node instanceof TclWhileStatement) {
				fold = true;
				TclWhileStatement statement = (TclWhileStatement) node;
				Block block = statement.getBlock();
				if (block != null) {
					children = block.getStatements();
				}
			} else if (node instanceof TclForeachStatement) {
				fold = true;
				TclForeachStatement statement = (TclForeachStatement) node;
				Block block = statement.getBlock();
				if (block != null) {
					children = block.getStatements();
				}
			} else if (node instanceof TclForStatement) {
				fold = true;
				TclForStatement statement = (TclForStatement) node;
				Block block = statement.getBlock();
				if (block != null) {
					children = block.getStatements();
				}
			}
			if (fold) {
				result.add(new CodeBlock(node, new Region(offset
						+ node.sourceStart(), node.sourceEnd()
						- node.sourceStart())));
			}
			if (children != null && children.size() > 0) {
				traverse(result, children, offset, code);
			}
		}
	}

	@Override
	protected String getCommentPartition() {
		return TclPartitions.TCL_COMMENT;
	}

	@Override
	protected ILog getLog() {
		return TclUI.getDefault().getLog();
	}

	@Override
	protected String getPartition() {
		return TclPartitions.TCL_PARTITIONING;
	}

	@Override
	protected IPartitionTokenScanner getPartitionScanner() {
		return new TclPartitionScanner();
	}

	@Override
	protected String[] getPartitionTypes() {
		return TclPartitions.TCL_PARTITION_TYPES;
	}

	@Override
	protected String getNatureId() {
		return TclNature.NATURE_ID;
	}

	@Override
	protected void initializePreferences(IPreferenceStore store) {
		super.initializePreferences(store);

		fInitCollapseOtherBlocks = store
				.getBoolean(TclPreferenceConstants.EDITOR_FOLDING_INIT_OTHER);

		fBlockFolding = store
				.getInt(TclPreferenceConstants.EDITOR_FOLDING_BLOCKS);

		fBlockExcludeList = initializeList(store,
				TclPreferenceConstants.EDITOR_FOLDING_EXCLUDE_LIST);
		fBlockIncludeList = initializeList(store,
				TclPreferenceConstants.EDITOR_FOLDING_INCLUDE_LIST);
	}

	@Override
	protected String getInitiallyFoldClassesKey() {
		return TclPreferenceConstants.EDITOR_FOLDING_INIT_NAMESPACES;
	}

	@Override
	protected String getInitiallyFoldMethodsKey() {
		return TclPreferenceConstants.EDITOR_FOLDING_INIT_BLOCKS;
	}

	@Override
	protected boolean initiallyCollapse(ASTNode s) {
		if (s instanceof TclStatement || s instanceof ITclStatementLookLike) {
			TclStatement statement = null;
			if (s instanceof ITclStatementLookLike) {
				statement = ((ITclStatementLookLike) s).getStatement();
			} else {
				statement = (TclStatement) s;
			}
			if (statement == null) {
				return false;
			}
			if (!(statement.getAt(0) instanceof SimpleReference)) {
				return false;
			}

			String name = null;
			name = ((SimpleReference) statement.getAt(0)).getName();
			if (name.equals("namespace")) {
				return fInitCollapseClasses;
			}
		}

		if (mayCollapse(s)) {
			return fInitCollapseOtherBlocks;
		}

		return super.initiallyCollapse(s);
	}

	private enum CommentType {
		SHEBANG, HEADER
	}

	private static class CommentRegion extends Region {

		final CommentType commentType;

		public CommentRegion(CommentType commentType, int offset, int length) {
			super(offset, length);
			this.commentType = commentType;
		}

		/**
		 * @param commentType
		 * @param region
		 */
		public CommentRegion(CommentType commentType, IRegion region) {
			super(region.getOffset(), region.getLength());
			this.commentType = commentType;
		}

	}

	@Override
	protected void prepareRegions(Document d, List<IRegion> regions) {
		if (regions.isEmpty()) {
			return;
		}
		IRegion region = regions.get(0);
		if (region.getOffset() != 0) {
			return;
		}
		try {
			String content = d.get(region.getOffset(), region.getLength());
			if (!content.startsWith("#!")) {
				return;
			}
			String line0 = d.get(d.getLineOffset(0), d.getLineLength(0));
			if (Pattern.compile("/(sh|bash|dash|tcsh)").matcher(line0).find()) {
				int i = 1;
				for (;;) {
					int offset = d.getLineOffset(i);
					final int length = d.getLineLength(i);
					String line = d.get(offset, length).trim();
					if (!line.startsWith("#") && !line.endsWith("\\")) {
						break;
					}
					final String delimiter = d.getLineDelimiter(i);
					if (delimiter != null) {
						offset += delimiter.length();
					}
					if (offset + length >= region.getLength()) {
						break;
					}
					++i;
				}
				final IRegion line = d.getLineInformation(i);
				IRegion shebang = new CommentRegion(CommentType.SHEBANG, 0,
						line.getOffset() + line.getLength());
				int headerOffset = line.getOffset() + d.getLineLength(i);
				final CommentRegion header = new CommentRegion(
						CommentType.HEADER, headerOffset, region.getLength()
								- headerOffset);
				regions.set(0, shebang);
				if (isMultilineRegion(d, header)) {
					regions.add(1, header);
				}
			}
		} catch (BadLocationException e) {
			// return
		}
	}

	@Override
	protected IRegion alignRegion(IRegion region,
			FoldingStructureComputationContext ctx) {
		final IRegion result = super.alignRegion(region, ctx);
		if (result != region && region instanceof CommentRegion) {
			return new CommentRegion(((CommentRegion) region).commentType,
					result);
		}
		return result;
	}

	@Override
	protected boolean initiallyCollapseComments(IRegion region,
			FoldingStructureComputationContext ctx) {
		if (ctx.allowCollapsing()) {
			if (region instanceof CommentRegion) {
				if (((CommentRegion) region).commentType == CommentType.SHEBANG) {
					return false;
				}
			}
			return isHeaderRegion(region, ctx) ? fInitCollapseHeaderComments
					: fInitCollapseComments;
		}
		return false;
	}

	@Override
	protected boolean isHeaderRegion(IRegion region,
			FoldingStructureComputationContext ctx) {
		if (region instanceof CommentRegion) {
			return ((CommentRegion) region).commentType == CommentType.HEADER;
		}
		return super.isHeaderRegion(region, ctx);
	}

	protected boolean canFold(String name) {
		switch (fBlockFolding) {
		case TclPreferenceConstants.EDITOR_FOLDING_BLOCKS_OFF: {
			if (name.equals("proc") || name.equals("namespace")) {
				return true;
			}

			return false;
		}
		case TclPreferenceConstants.EDITOR_FOLDING_BLOCKS_INCLUDE: {
			if (fBlockIncludeList.contains(name)) {
				return true;
			}

			return false;
		}
		case TclPreferenceConstants.EDITOR_FOLDING_BLOCKS_EXCLUDE: {
			if (fBlockExcludeList.contains(name)) {
				return false;
			}

			return true;
		}
		}
		return false;
	}

	@Override
	protected boolean mayCollapse(ASTNode s,
			FoldingStructureComputationContext ctx) {
		return mayCollapse(s);
	}

	protected boolean mayCollapse(ASTNode s) {
		if (s instanceof TypeDeclaration) {
			return canFold("namespace");
		} else if (s instanceof MethodDeclaration) {
			return canFold("proc");
		} else if (s instanceof IfStatement) {
			return canFold("if");
		} else if (s instanceof TclSwitchStatement) {
			return canFold("switch");
		} else if (s instanceof TclFoldBlock) {
			return canFold("switch");
		} else if (s instanceof TclCatchStatement) {
			return canFold("catch");
		} else if (s instanceof TclWhileStatement) {
			return canFold("while");
		} else if (s instanceof TclForeachStatement) {
			return canFold("foreach");
		} else if (s instanceof TclForStatement) {
			return canFold("for");
		} else if (s instanceof TclStatement) {
			TclStatement statement = (TclStatement) s;
			if (!(statement.getAt(0) instanceof SimpleReference)) {
				return false;
			}

			String name = null;
			name = ((SimpleReference) statement.getAt(0)).getName();
			return canFold(name);
		}

		return false;
	}

	private List<String> initializeList(IPreferenceStore store, String key) {
		String t = store.getString(key);
		String[] items = t.split(","); //$NON-NLS-1$

		List<String> list = new ArrayList<String>(items.length);
		for (int i = 0; i < items.length; i++) {
			if (items[i].trim().length() > 0) {
				list.add(items[i]);
			}
		}
		return list;
	}

}
