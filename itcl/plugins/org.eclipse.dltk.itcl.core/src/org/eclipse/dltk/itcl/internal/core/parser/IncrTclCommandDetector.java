package org.eclipse.dltk.itcl.internal.core.parser;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.mixin.IMixinElement;
import org.eclipse.dltk.core.mixin.IMixinRequestor;
import org.eclipse.dltk.itcl.internal.core.IIncrTclModifiers;
import org.eclipse.dltk.itcl.internal.core.classes.IncrTclClassesManager;
import org.eclipse.dltk.itcl.internal.core.search.mixin.model.IncrTclClass;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.core.ITclCommandDetector;
import org.eclipse.dltk.tcl.core.ITclCommandDetectorExtension;
import org.eclipse.dltk.tcl.core.ITclParser;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.internal.core.search.mixin.TclMixinModel;

public class IncrTclCommandDetector implements ITclCommandDetector,
		ITclCommandDetectorExtension {
	private final static String[] itclCommands = new String[] { "class",
			"body", "code", "configbody", "delete", "ensemble", "find",
			"local", "scope" };
	private String prefix = "itcl::";
	private boolean runtimeModel = false;
	private Set names = new HashSet();

	public static class IncrTclGlobalClassParameter {
		private String name;

		public IncrTclGlobalClassParameter(String name) {
			this.name = name;
		}

		public IModelElement resolveElement() {
			IMixinElement[] find = TclMixinModel
					.getInstance()
					.getMixin(null)
					.find(
							name.replaceAll("::",
									IMixinRequestor.MIXIN_NAME_SEPARATOR), 1000);
			if (find.length > 0) {
				for (int i = 0; i < find.length; i++) {
					Object[] allObjects = find[i].getAllObjects();
					for (int j = 0; j < allObjects.length; j++) {
						if (allObjects[j] != null
								&& allObjects[j] instanceof IncrTclClass) {
							IncrTclClass class_ = (IncrTclClass) allObjects[j];
							return class_.getModelElement();
						}
					}
				}
			}
			return null;
		}

		public String getClassName() {
			return this.name;
		}
	}

	public IncrTclCommandDetector() {
	}

	/**
	 * 1) Detect of core itcl commands
	 * 
	 * 2) Detect itcl class access
	 * 
	 * 3) Detect itcl object creations
	 * 
	 * 4) Detect itcl instances method access, etc.
	 * 
	 */
	public CommandInfo detectCommand(TclStatement statement,
			ModuleDeclaration module, ITclParser parser, ASTNode decl) {
		if (statement.getCount() == 0) {
			return null;
		}
		Expression commandName = statement.getAt(0);
		if (commandName instanceof SimpleReference) {
			String value = ((SimpleReference) commandName).getName();
			for (int i = 0; i < itclCommands.length; i++) {
				if (itclCommands[i].equals(value)
						|| (prefix + itclCommands[i]).equals(value)
						|| ("::" + prefix + itclCommands[i]).equals(value)) {
					return new CommandInfo("#itcl#" + itclCommands[i], null);
				}
			}
			return checkInstanceOperations(module, decl, statement, parser);
		}
		return null;
	}

	private CommandInfo checkInstanceOperations(ModuleDeclaration module,
			ASTNode parent, TclStatement statement, ITclParser parser) {
		if (runtimeModel) {
			return null;
		}
		Expression commandName = statement.getAt(0);
		if (!(commandName instanceof SimpleReference)) {
			return null;
		}
		String commandNameValue = ((SimpleReference) commandName).getName();
		String[] names = null;
		if (commandNameValue.startsWith("::")) {
			names = commandNameValue.substring(2).split("::");
		} else {
			names = commandNameValue.split("::");
		}

		boolean found = false;
		for (int i = 0; i < names.length; i++) {
			if (this.names.contains(names[i])) {
				found = true;
				break;
			}
		}
		if (found) {
			TypeDeclaration type = TclParseUtil.findXOTclTypeDeclarationFrom(
					module, parent, commandNameValue);
			if (statement.getCount() == 1) {
				return null;
			}
			Expression arg = statement.getAt(1);
			if (type != null) {
				if (arg instanceof SimpleReference) {
					return check(type, (SimpleReference) arg);
				}
			}
		}

		// Lets check possibly this is method call for existing instance
		// variable.
		if (found) {
			FieldDeclaration variable = IncrTclParseUtil
					.findInstanceVariableDeclarationFrom(module, parent,
							commandNameValue);
			if (variable != null) {
				// Add support of procs etc.
				return new CommandInfo("#itcl#$methodCall", variable);
			}
		}

		// class list check operation.
		if (IncrTclClassesManager.getDefault().isClass(commandNameValue)) {
			IncrTclGlobalClassParameter param = new IncrTclGlobalClassParameter(
					commandNameValue);
			return new CommandInfo("#itcl#$newInstance", param);
		}

		return null;
	}

	private CommandInfo check(TypeDeclaration type, SimpleReference arg) {
		if ((type.getModifiers() & IIncrTclModifiers.AccIncrTcl) == 0) {
			return null;
		}
		// We need to understand what specified type has't contain method or
		// proc with argument name
		String value = arg.getName();
		MethodDeclaration[] methods = type.getMethods();
		for (int i = 0; i < methods.length; i++) {
			if ((methods[i].getModifiers() & IIncrTclModifiers.AccIncrTclProc) != 0) {
				if (methods[i].getName().equals(value)) {
					return new CommandInfo("#itcl#$methodCall", type);
				}
			}
		}
		// String value = ((SimpleReference) arg).getName();
		return new CommandInfo("#itcl#$newInstance", type);
	}

	public void setBuildRuntimeModelFlag(boolean value) {
		this.runtimeModel = value;
	}

	public void processASTNode(ASTNode node) {
		String name = null;
		if (node instanceof FieldDeclaration) {
			FieldDeclaration decl = (FieldDeclaration) node;
			name = decl.getName();
		} else if (node instanceof TypeDeclaration) {
			TypeDeclaration decl = (TypeDeclaration) node;
			name = decl.getName();
		}
		if (name != null) {
			String[] names = null;
			if (name.startsWith("::")) {
				names = name.substring(2).split("::");
			} else {
				names = name.split("::");
			}
			for (int i = 0; i < names.length; i++) {
				this.names.add(names[i]);
			}
		}
	}
}
