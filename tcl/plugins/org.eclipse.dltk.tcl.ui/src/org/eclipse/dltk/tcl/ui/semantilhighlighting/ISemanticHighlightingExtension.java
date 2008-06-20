package org.eclipse.dltk.tcl.ui.semantilhighlighting;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ui.editor.highlighting.ISemanticHighlightingRequestor;
import org.eclipse.dltk.ui.editor.highlighting.SemanticHighlighting;

public interface ISemanticHighlightingExtension {

	SemanticHighlighting[] getHighlightings();

	void processNode(ASTNode node, ISemanticHighlightingRequestor requestor);

}
