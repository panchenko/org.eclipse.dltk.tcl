package org.eclipse.dltk.tcl.parser.tests;

import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.tcl.internal.parser.NewTclSourceParser;
import org.eclipse.dltk.tcl.internal.parser.TclSourceParser;

import junit.framework.TestCase;

public class NewTclParserTests extends TestCase {
	public void test001() throws Exception {
		String content = "proc alfa {     alfa {    beta } } {}";
		TclSourceParser oldParser = new TclSourceParser();
		ModuleDeclaration oldAST = oldParser.parse(null, content.toCharArray(),
				null);

		NewTclSourceParser parser = new NewTclSourceParser();
		ModuleDeclaration ast = parser.parse(null, content.toCharArray(), null);
		List childs = ast.getStatements();
		TestCase.assertEquals(1, childs.size());
		MethodDeclaration method = (MethodDeclaration) childs.get(0);
		List<ASTNode> arguments = method.getArguments();
		ASTNode arg1 = arguments.get(0);
		TestCase.assertEquals(16, arg1.sourceStart());
	}
}
