package org.eclipse.dltk.tcl.core.extensions;

import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.tcl.internal.parser.TclSourceElementRequestVisitor;
import org.eclipse.dltk.tcl.internal.parser.TclSourceElementRequestVisitor.ExitFromType;

public interface ISourceElementRequestVisitorExtension {

	int getModifiers(Declaration s);

	/**
	 * @since 2.0
	 */
	boolean isConstructor(MethodDeclaration declaration);

	boolean visit(Statement statement, TclSourceElementRequestVisitor original);

	boolean extendedExitRequired(MethodDeclaration method,
			TclSourceElementRequestVisitor tclSourceElementRequestVisitor);

	ExitFromType getExitExtended(MethodDeclaration method,
			TclSourceElementRequestVisitor tclSourceElementRequestVisitor);

	ExitFromType processField(FieldDeclaration decl,
			TclSourceElementRequestVisitor tclSourceElementRequestVisitor);

	boolean skipMethod(MethodDeclaration method,
			TclSourceElementRequestVisitor tclSourceElementRequestVisitor);
}
