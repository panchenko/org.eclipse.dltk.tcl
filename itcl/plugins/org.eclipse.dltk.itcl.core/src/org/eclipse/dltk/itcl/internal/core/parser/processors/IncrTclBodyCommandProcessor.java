package org.eclipse.dltk.itcl.internal.core.parser.processors;

import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.itcl.internal.core.IIncrTclModifiers;
import org.eclipse.dltk.itcl.internal.core.classes.IncrTclClassesManager;
import org.eclipse.dltk.itcl.internal.core.parser.ast.IncrTclBodyDeclaration;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.core.AbstractTclCommandProcessor;
import org.eclipse.dltk.tcl.core.ITclParser;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.core.ast.ExtendedTclMethodDeclaration;
import org.eclipse.dltk.tcl.internal.core.codeassist.TclVisibilityUtils;

public class IncrTclBodyCommandProcessor extends AbstractTclCommandProcessor {

	public IncrTclBodyCommandProcessor() {
	}

	public ASTNode process(TclStatement statement, ITclParser parser,
			ASTNode parent) {
		if (statement == null || (statement.getCount() == 0)) {
			return null;
		}
		if (statement.getCount() < 4) {
			this.report(parser, "Wrong number of arguments", statement
					.sourceStart(), statement.sourceEnd(),
					ProblemSeverities.Error);
			addToParent(parent, statement);
			return statement;
		}
		Expression procName = statement.getAt(1);

		String sName = IncrTclUtils.extractMethodName(procName);
		if (sName == null || sName.length() == 0) {
			this.report(parser, "Wrong number of arguments", statement
					.sourceStart(), statement.sourceEnd(),
					ProblemSeverities.Error);
			addToParent(parent, statement);
			return statement;
		}
		Expression procArguments = statement.getAt(2);
		Expression procCode = statement.getAt(3);

		List arguments = IncrTclUtils.extractMethodArguments(procArguments);

		IncrTclBodyDeclaration method = new IncrTclBodyDeclaration(statement
				.sourceStart(), statement.sourceEnd());
		int index = sName.lastIndexOf("::");
		if (index == -1) {
			this.report(parser, "Wrong Class name", statement.sourceStart(),
					statement.sourceEnd(), ProblemSeverities.Error);
			return statement;
		}
		String className = sName.substring(0, index);
		String methodName = sName.substring(index + 2);
		method.setName(methodName);
		method.setNameStart(procName.sourceStart());
		method.setNameEnd(procName.sourceEnd());
		method.acceptArguments(arguments);
		method.setKind(ExtendedTclMethodDeclaration.KIND_INSTPROC);

		// For correct modifiers we need to add
		IncrTclUtils.parseBlockAdd(parser, procCode, method);

		// method.setModifier(IIncrTclModifiers.AccIncrTcl |
		// IIncrTclModifiers.AccPublic);
		if (TclVisibilityUtils.isPrivate(sName)) {
			method.setModifier(IIncrTclModifiers.AccIncrTcl
					| Modifiers.AccPrivate);
		} else {
			method.setModifier(IIncrTclModifiers.AccIncrTcl
					| Modifiers.AccPublic);
		}
		ModuleDeclaration module = this.getModuleDeclaration();
		TypeDeclaration possiblyType = TclParseUtil.findTclTypeDeclarationFrom(
				module, parent, className, false);
		if (possiblyType != null) {
			MethodDeclaration[] methods = possiblyType.getMethods();
			boolean found = false;
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals(methodName)) {
					method.setModifier(methods[i].getModifiers());
					method.setDeclaringType(possiblyType);
					method.setDeclaringTypeName(TclParseUtil.getElementFQN(
							possiblyType, "::", module));
					this.addToParent(parent, method);
					found = true;
					break;
				}
			}
			if (!found) {
				// May be not correct
				this.addToParent(parent, method);
				this.report(parser, "Function " + method.getName()
						+ " is not defined in class " + possiblyType.getName(),
						statement.sourceStart(), statement.sourceEnd(),
						ProblemSeverities.Error);
			}
		} else {
			if (!IncrTclClassesManager.getDefault().isClass(className)) {
				this.report(parser, "Body declaration for unknown class",
						statement.sourceStart(), statement.sourceEnd(),
						ProblemSeverities.Error);
			}
			this.addToParent(parent, method);
		}

		return method;
	}

}
