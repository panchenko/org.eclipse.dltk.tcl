package org.eclipse.dltk.tcl.internal.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.ui.semantilhighlighting.ISemanticHighlightingExtension;
import org.eclipse.dltk.ui.editor.highlighting.ASTSemanticHighlighter;
import org.eclipse.dltk.ui.editor.highlighting.SemanticHighlighting;

public class TclSemanticPositionUpdater extends ASTSemanticHighlighter {

	private final ISemanticHighlightingExtension[] extensions;

	public TclSemanticPositionUpdater(
			ISemanticHighlightingExtension[] extensions) {
		this.extensions = extensions;
	}

	public SemanticHighlighting[] getSemanticHighlightings() {
		List<SemanticHighlighting> highlightings = new ArrayList<SemanticHighlighting>();
		for (int i = 0; i < extensions.length; i++) {
			SemanticHighlighting[] hl = extensions[i].getHighlightings();
			if (hl != null) {
				highlightings.addAll(Arrays.asList(hl));
			}
		}
		SemanticHighlighting[] ret = new SemanticHighlighting[highlightings
				.size()];
		for (int i = 0; i < highlightings.size(); i++)
			ret[i] = highlightings.get(i);

		return ret;
	}

	protected String getNature() {
		return TclNature.NATURE_ID;
	}

	@Override
	protected boolean doHighlighting(IModuleSource code) throws Exception {
		ModuleDeclaration declaration = (ModuleDeclaration) parseCode(code);
		if (declaration != null) {
			declaration.traverse(new ASTVisitor() {

				public boolean visitGeneral(ASTNode node) throws Exception {
					for (int i = 0; i < extensions.length; i++) {
						extensions[i].processNode(node,
								TclSemanticPositionUpdater.this);
					}
					return true;
				}

			});
			for (int i = 0; i < extensions.length; ++i) {
				if (extensions[i] instanceof DefaultTclSemanticHighlightingExtension) {
					DefaultTclSemanticHighlightingExtension hl = (DefaultTclSemanticHighlightingExtension) extensions[i];
					hl.doOtherHighlighting(code,
							TclSemanticPositionUpdater.this);
				}
			}
			return true;
		}
		return false;
	}
}
