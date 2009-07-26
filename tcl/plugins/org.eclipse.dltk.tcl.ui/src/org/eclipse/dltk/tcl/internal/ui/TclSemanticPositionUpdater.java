package org.eclipse.dltk.tcl.internal.ui;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.env.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.ast.TclModuleDeclaration;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.core.codeassist.TclVisibilityUtils;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.ui.semantilhighlighting.ISemanticHighlightingExtension;
import org.eclipse.dltk.ui.editor.highlighting.ASTSemanticHighlighter;
import org.eclipse.dltk.ui.editor.highlighting.ISemanticHighlightingRequestor;

public class TclSemanticPositionUpdater extends ASTSemanticHighlighter {

	private final ISemanticHighlightingExtension[] extensions;
	private final ISemanticHighlightingRequestor[] requestors;

	private static class SemanticPositionRequestorExtension implements
			ISemanticHighlightingRequestor {

		private final ISemanticHighlightingRequestor requestor;
		private final int offset;

		/**
		 * @param requestor
		 * @param offset
		 */
		public SemanticPositionRequestorExtension(
				ISemanticHighlightingRequestor requestor, int offset) {
			this.offset = offset;
			this.requestor = requestor;
		}

		public void addPosition(int start, int end, int highlightingIndex) {
			requestor.addPosition(start, end, highlightingIndex + offset);
		}

	}

	public TclSemanticPositionUpdater(
			ISemanticHighlightingExtension[] extensions) {
		this.extensions = extensions;
		this.requestors = new ISemanticHighlightingRequestor[extensions.length];
		int offset = 0;
		for (int i = 0; i < extensions.length; ++i) {
			requestors[i] = new SemanticPositionRequestorExtension(this, offset);
			offset += extensions[i].getHighlightings().length;
		}
	}

	protected ASTVisitor createVisitor(
			org.eclipse.dltk.compiler.env.ISourceModule sourceCode)
			throws ModelException {
		return new ASTVisitor() {

			public boolean visitGeneral(ASTNode node) throws Exception {
				for (int i = 0; i < extensions.length; i++) {
					extensions[i].processNode(node, requestors[i]);
				}
				return true;
			}

		};
	}

	protected String getNature() {
		return TclNature.NATURE_ID;
	}

	@Override
	protected boolean doHighlighting(ISourceModule code) throws Exception {
		boolean result = super.doHighlighting(code);
		for( int i = 0; i < extensions.length;++i) {
			if( extensions[i] instanceof DefaultTclSemanticHighlightingExtension ) {
				DefaultTclSemanticHighlightingExtension hl = (DefaultTclSemanticHighlightingExtension) extensions[i];
				hl.doOtherHighlighting(code, requestors[i]);
			}
		}
		return result;
	}
}
