package org.eclipse.dltk.tcl.internal.ui;

import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.ast.ASTListNode;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.tcl.internal.ui.text.TclTextTools;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.tcl.ui.semantilhighlighting.ISemanticHighlightingExtension;
import org.eclipse.dltk.ui.editor.highlighting.HighlightedPosition;
import org.eclipse.dltk.ui.editor.highlighting.ISemanticHighlightingRequestor;
import org.eclipse.dltk.ui.editor.highlighting.SemanticHighlighting;

public class DefaultTclSemanticHighlightingExtension implements
		ISemanticHighlightingExtension {

	private SemanticHighlighting[] highlightings = new SemanticHighlighting[] {
			new TclTextTools.SH(TclPreferenceConstants.EDITOR_PROCEDURES_COLOR,	null),			
			new TclTextTools.SH(TclPreferenceConstants.EDITOR_ARGUMENTS_COLOR, null),
			new TclTextTools.SH(TclPreferenceConstants.EDITOR_CLASSES_COLOR, null),
			new TclTextTools.SH(TclPreferenceConstants.EDITOR_BASE_CLASS_COLOR, null)
	};

	private static final int HL_PROCEDURES = 0;
	private static final int HL_ARGUMENTS = 1;
	private static final int HL_CLASSES = 2;
	private static final int HL_BASE_CLASSES = 3;
	
	public DefaultTclSemanticHighlightingExtension() {
	}

	public HighlightedPosition[] calculatePositions(ASTNode node,
			ISemanticHighlightingRequestor requestor) {

		// Check Tcl procedures
		if (node instanceof MethodDeclaration) {

			MethodDeclaration m = (MethodDeclaration) node;
			requestor.addPosition(m.getNameStart(), m.getNameEnd(), HL_PROCEDURES);

		}

		// Check procedure arguments
		if (node instanceof Argument) {
			Argument m = (Argument) node;
			requestor.addPosition(m.getNameStart(), m.getNameEnd(), HL_ARGUMENTS);

		}

		// Check IncrTcl and XOTcl classes
		if (node instanceof TypeDeclaration) {

			TypeDeclaration t = (TypeDeclaration) node;
			List children;

			// Handle base classes highlighting
			ASTListNode s = t.getSuperClasses();

			if (s != null && s.getChilds() != null) {
				children = s.getChilds();
				Iterator it = children.iterator();
				while (it.hasNext()) {
					ASTNode n = (ASTNode) it.next();

					requestor.addPosition(n.sourceStart(), n.sourceEnd(), HL_BASE_CLASSES);

				}
			}

			requestor.addPosition(t.getNameStart(), t.getNameEnd(), HL_CLASSES);
		}

		return null;

	}

	public void processNode(ASTNode node,
			ISemanticHighlightingRequestor requestor) {
		calculatePositions(node, requestor);

	}

	public SemanticHighlighting[] getHighlightings() {
		return highlightings;
	}
}
