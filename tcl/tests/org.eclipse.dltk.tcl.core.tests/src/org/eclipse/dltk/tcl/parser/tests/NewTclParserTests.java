package org.eclipse.dltk.tcl.parser.tests;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.env.ModuleSource;
import org.eclipse.dltk.tcl.internal.parser.NewTclSourceParser;
import org.eclipse.dltk.tcl.internal.parser.TclSourceParser;

public class NewTclParserTests extends TestCase {
	public void test001() throws Exception {
		String content = "proc alfa {     alfa {    beta } } {}";
		TclSourceParser oldParser = new TclSourceParser();
		ModuleSource input = new ModuleSource(content);
		ModuleDeclaration oldAST = oldParser.parse(input, null);

		NewTclSourceParser parser = new NewTclSourceParser();
		ModuleDeclaration ast = parser.parse(input, null);
		List childs = ast.getStatements();
		TestCase.assertEquals(1, childs.size());
		MethodDeclaration method = (MethodDeclaration) childs.get(0);
		List arguments = method.getArguments();
		ASTNode arg1 = (ASTNode) arguments.get(0);
		TestCase.assertEquals(16, arg1.sourceStart());
	}
}
