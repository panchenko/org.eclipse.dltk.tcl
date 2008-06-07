package org.eclipse.dltk.tcl.core.extensions;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.search.matching.PatternLocator;
import org.eclipse.dltk.internal.core.search.matching.MatchingNodeSet;
import org.eclipse.dltk.tcl.internal.core.search.TclMatchLocator;

public interface IMatchLocatorExtension {
	// This is match locator feature extension
	public void visitGeneral(ASTNode node, PatternLocator locator,
			MatchingNodeSet nodeSet);

	public IModelElement createMethodHandle(ISourceModule parent,
			MethodDeclaration method, TclMatchLocator tclMatchLocator);

}
