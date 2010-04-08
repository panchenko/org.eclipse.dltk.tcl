/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.ui.tests.folding;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.core.tests.util.StringList;
import org.eclipse.dltk.tcl.internal.ui.text.folding.TclFoldingStructureProvider;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.tcl.ui.tests.TclUITestsPlugin;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider.FoldingStructureComputationContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;

public class TclFoldingTest extends TestCase {

	private class MyTclASTFoldingStructureProvider extends
			TclFoldingStructureProvider {

		@Override
		protected FoldingStructureComputationContext createInitialContext() {
			initializePreferences(fStore);
			return createContext(true);
		}

		@Override
		protected FoldingStructureComputationContext createContext(
				boolean allowCollapse) {
			return new FoldingStructureComputationContext(fDocument,
					new ProjectionAnnotationModel(), allowCollapse);
		}

		final Document fDocument = new Document();

		@Override
		protected boolean computeFoldingStructure(String contents,
				FoldingStructureComputationContext ctx) {
			fDocument.set(contents);
			return super.computeFoldingStructure(contents, ctx);
		}

	};

	IPreferenceStore fStore;
	MyTclASTFoldingStructureProvider provider;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		fStore = TclUITestsPlugin.getDefault().getPreferenceStore();
		TclPreferenceConstants.initializeDefaultValues(fStore);
		provider = new MyTclASTFoldingStructureProvider();
	}

	private List<Position> compute(String contents) {
		final FoldingStructureComputationContext ctx = provider
				.createInitialContext();
		assertTrue(provider.computeFoldingStructure(contents, ctx));
		return new ArrayList<Position>(ctx.getMap().values());
	}

	public void test0() throws Exception {
		fStore.setValue(TclPreferenceConstants.EDITOR_FOLDING_LINES_LIMIT, 2);
		StringList content = new StringList();
		content.add("#ab");
		content.add("#dc");
		List<Position> result = compute(content.toString());
		assertEquals(1, result.size());
		assertEquals(0, result.get(0).getOffset());
		assertEquals(content.length(), result.get(0).getLength());
	}

	public void testJoinNewLinesOn() throws Exception {
		StringList content = new StringList();
		content.add("#ab");
		content.add("");
		content.add("#dc");
		fStore.setValue(
				PreferenceConstants.EDITOR_COMMENT_FOLDING_JOIN_NEWLINES, true);
		List<Position> result = compute(content.toString());
		assertEquals(1, result.size());
	}

	public void testJoinNewLinesOff() throws Exception {
		StringList content = new StringList();
		content.add("#ab");
		content.add("");
		content.add("#dc");
		fStore
				.setValue(
						PreferenceConstants.EDITOR_COMMENT_FOLDING_JOIN_NEWLINES,
						false);
		List<Position> result = compute(content.toString());
		assertEquals(0, result.size());
	}

	public void test2() throws Exception {
		String content = "#ab\n\n#dc\n";
		fStore.setValue(
				PreferenceConstants.EDITOR_COMMENT_FOLDING_JOIN_NEWLINES, true);
		List<Position> result = compute(content);
		assertEquals(1, result.size());
	}

	public void test3() throws Exception {
		fStore.setValue(TclPreferenceConstants.EDITOR_FOLDING_LINES_LIMIT, 2);
		StringList content = new StringList();
		content.add(" namespace eval NM {");
		content.add("    # headercomment");
		content.add("    # here");
		content.add("    # ...");
		content.add("}");
		content.add("proc foo {} {");
		content.add("    if $a {");
		content.add("       doo");
		content.add("       doo2");
		content.add("       anothercmdblock xxx {");
		content.add("            #...");
		content.add("       }");
		content.add(");    }");
		content.add("}");
		fStore.setValue(
				PreferenceConstants.EDITOR_COMMENT_FOLDING_JOIN_NEWLINES, true);
		fStore.setValue(TclPreferenceConstants.EDITOR_FOLDING_BLOCKS,
				TclPreferenceConstants.EDITOR_FOLDING_BLOCKS_EXCLUDE);
		fStore.setValue(TclPreferenceConstants.EDITOR_FOLDING_EXCLUDE_LIST, "");
		List<Position> result = compute(content.toString());
		assertEquals(5, result.size());
	}

	public void test4() throws Exception {
		StringList content = new StringList();
		content.add("namespace eval NM {");
		content.add("    # headercomment");
		content.add("    # here");
		content.add("    # ...");
		content.add("}");
		content.add("proc foo {} {");
		content.add("    if $a {");
		content.add("       doo");
		content.add("       doo2");
		content.add("       anothercmdblock xxx {");
		content.add("            #...");
		content.add("       }");
		content.add("    }");
		content.add("}");
		fStore.setValue(
				PreferenceConstants.EDITOR_COMMENT_FOLDING_JOIN_NEWLINES, true);
		fStore.setValue(TclPreferenceConstants.EDITOR_FOLDING_BLOCKS,
				TclPreferenceConstants.EDITOR_FOLDING_BLOCKS_INCLUDE);
		fStore.setValue(TclPreferenceConstants.EDITOR_FOLDING_INCLUDE_LIST,
				"anothercmdblock");
		List<Position> result = compute(content.toString());
		assertEquals(2, result.size());
	}

	public void testSheBang() throws Exception {
		fStore.setValue(TclPreferenceConstants.EDITOR_FOLDING_LINES_LIMIT, 2);
		StringList content = new StringList();
		content.add("#!tclsh");
		content.add("#ab");
		content.add("#dc");
		List<Position> result = compute(content.toString());
		assertEquals(1, result.size());
		assertEquals(0, result.get(0).getOffset());
		assertEquals(content.length(), result.get(0).getLength());
	}
}
