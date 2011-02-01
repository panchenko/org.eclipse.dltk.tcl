package org.eclipse.dltk.xotcl.ui;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.internal.ui.text.TclTextTools;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.tcl.ui.semantilhighlighting.ISemanticHighlightingExtension;
import org.eclipse.dltk.ui.editor.highlighting.ISemanticHighlightingRequestor;
import org.eclipse.dltk.ui.editor.highlighting.SemanticHighlighting;
import org.eclipse.dltk.xotcl.core.ast.xotcl.XOTclDocumentationNode;

public class XOTclSemanticHighlightingExtension implements
		ISemanticHighlightingExtension {

	private final SemanticHighlighting[] highlightings = new SemanticHighlighting[] { new TclTextTools.SH(
			TclPreferenceConstants.EDITOR_SINGLE_LINE_COMMENT_COLOR, null, null) };

	private static final int HL_EOL_COMMENT = 0;

	public XOTclSemanticHighlightingExtension() {
	}

	public void processNode(ASTNode node,
			ISemanticHighlightingRequestor requestor) {
		if (node instanceof TclStatement) {
			TclStatement st = (TclStatement) node;
			if (st.getAt(0) instanceof SimpleReference
					&& ((SimpleReference) st.getAt(0)).getName().equals("@")) { //$NON-NLS-1$
				requestor
						.addPosition(
								st.sourceStart(),
								st.sourceEnd(),
								TclPreferenceConstants.EDITOR_SINGLE_LINE_COMMENT_COLOR);
			}
		} else if (node instanceof XOTclDocumentationNode) {
			requestor.addPosition(node.sourceStart(), node.sourceEnd(),
					TclPreferenceConstants.EDITOR_SINGLE_LINE_COMMENT_COLOR);
		}
	}

	public SemanticHighlighting[] getHighlightings() {
		return highlightings;
	}
}
