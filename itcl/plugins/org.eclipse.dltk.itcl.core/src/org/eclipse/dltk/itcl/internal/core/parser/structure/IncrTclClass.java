/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.itcl.internal.core.parser.structure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.compiler.IElementRequestor.FieldInfo;
import org.eclipse.dltk.compiler.IElementRequestor.MethodInfo;
import org.eclipse.dltk.compiler.IElementRequestor.TypeInfo;
import org.eclipse.dltk.itcl.internal.core.IIncrTclModifiers;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IClass;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IMember;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IMethod;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IVariable;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IMember.Visibility;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IMethod.MethodKind;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IVariable.VariableKind;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.impl.ClassImpl;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.impl.Method;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.impl.Variable;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHandler;
import org.eclipse.dltk.tcl.structure.ITclTypeResolver;
import org.eclipse.dltk.tcl.structure.TclModelProblem;

public class IncrTclClass extends AbstractTclCommandModelBuilder {

	public boolean process(TclCommand command, ITclModelBuildContext context)
			throws TclModelProblem {
		if (command.getArguments().size() != 2) {
			return false;
		}
		final TclArgument className = command.getArguments().get(0);
		if (!isSymbol(className)) {
			return false;
		}
		final ClassImpl clazz = new ClassImpl();
		// TODO parse without processors
		final Script classBody = context.parse(command.getArguments().get(1),
				ITclModelBuildContext.NO_TRAVERSE);
		processContent(clazz, classBody.getCommands(), context);
		final TypeInfo ti = new TypeInfo();
		ti.declarationStart = command.getStart();
		ti.nameSourceStart = className.getStart();
		ti.nameSourceEnd = className.getEnd() - 1;
		ti.modifiers = IIncrTclModifiers.AccIncrTcl;
		ti.superclasses = clazz.getSuperClasses();
		ITclTypeHandler resolvedType = context.get(ITclTypeResolver.class)
				.resolveType(ti, command.getEnd(), asSymbol(className));
		context.enterNamespace(resolvedType);
		clazz.setName(resolvedType.getNamespace());
		IncrTclNames.create(context).addType(clazz);
		for (IMember member : clazz.getMembers()) {
			if (member instanceof IMethod) {
				final IMethod method = (IMethod) member;
				MethodInfo mi = new MethodInfo();
				mi.name = member.getName();
				mi.modifiers = method.getModifiers();
				mi.isConstructor = method.getKind() == MethodKind.CONSTRUCTOR;
				mi.nameSourceStart = member.getNameStart();
				mi.nameSourceEnd = member.getNameEnd();
				mi.declarationStart = member.getStart();
				fillParameters(mi, method.getParameters());
				context.getRequestor().enterMethodRemoveSame(mi);
				for (TclArgument body : method.getBodies()) {
					context.parse(body);
				}
				context.getRequestor().exitMethod(member.getEnd());
			} else if (member instanceof IVariable) {
				final IVariable variable = (IVariable) member;
				final FieldInfo fi = new FieldInfo();
				fi.name = member.getName();
				fi.modifiers = variable.getModifiers();
				fi.nameSourceStart = variable.getNameStart();
				fi.nameSourceEnd = variable.getNameEnd();
				fi.declarationStart = variable.getStart();
				if (context.getRequestor().enterFieldCheckDuplicates(fi)) {
					context.getRequestor().exitField(variable.getEnd());
				}
			}
		}
		context.leaveNamespace(resolvedType);
		return false;
	}

	private void processContent(IClass clazz, List<TclCommand> bodyCommands,
			ITclModelBuildContext context) {
		for (TclCommand cmd : bodyCommands) {
			processContent(clazz, new CommandImpl(cmd), context);
		}
	}

	private void processContent(IClass clazz, ICommand cmd,
			ITclModelBuildContext context) {
		TclArgument cmdName = cmd.getName();
		if (isSymbol(cmdName)) {
			Handler handler = handlers.get(asSymbol(cmdName));
			if (handler != null) {
				handler.handle(clazz, cmd, context);
			}
		}
	}

	private interface Handler {
		void handle(IClass clazz, ICommand command,
				ITclModelBuildContext context);
	}

	private Map<String, Handler> handlers = new HashMap<String, Handler>();
	{
		handlers.put("inherit", new Handler() {
			public void handle(IClass clazz, ICommand command,
					ITclModelBuildContext context) {
				handleInherit(clazz, command);
			}
		});
		handlers.put("constructor", new Handler() {
			public void handle(IClass clazz, ICommand command,
					ITclModelBuildContext context) {
				handleConstructor(clazz, command);
			}
		});
		handlers.put("destructor", new Handler() {
			public void handle(IClass clazz, ICommand command,
					ITclModelBuildContext context) {
				handleDestructor(clazz, command);
			}
		});
		handlers.put("method", new Handler() {
			public void handle(IClass clazz, ICommand command,
					ITclModelBuildContext context) {
				handleProc(clazz, command, MethodKind.METHOD);
			}
		});
		handlers.put("proc", new Handler() {
			public void handle(IClass clazz, ICommand command,
					ITclModelBuildContext context) {
				handleProc(clazz, command, MethodKind.PROC);
			}
		});
		handlers.put("variable", new Handler() {
			public void handle(IClass clazz, ICommand command,
					ITclModelBuildContext context) {
				handleVariable(clazz, command, VariableKind.VARIABLE);
			}
		});
		handlers.put("common", new Handler() {
			public void handle(IClass clazz, ICommand command,
					ITclModelBuildContext context) {
				handleVariable(clazz, command, VariableKind.COMMON);
			}
		});
		handlers.put("public", new Handler() {
			public void handle(IClass clazz, ICommand command,
					ITclModelBuildContext context) {
				handleVisibility(clazz, command, context, Visibility.PUBLIC);
			}
		});
		handlers.put("protected", new Handler() {
			public void handle(IClass clazz, ICommand command,
					ITclModelBuildContext context) {
				handleVisibility(clazz, command, context, Visibility.PROTECTED);
			}
		});
		handlers.put("private", new Handler() {
			public void handle(IClass clazz, ICommand command,
					ITclModelBuildContext context) {
				handleVisibility(clazz, command, context, Visibility.PRIVATE);
			}
		});
	}

	/**
	 * @param clazz
	 * @param command
	 */
	protected void handleInherit(IClass clazz, ICommand command) {
		if (command.getArgumentCount() == 0) {
			return;
		}
		for (TclArgument argument : command.getArguments()) {
			if (isSymbol(argument)) {
				clazz.addSuperclass(asSymbol(argument));
			}
		}
	}

	/**
	 * @param clazz
	 * @param command
	 * @param kind
	 */
	protected void handleVariable(IClass clazz, ICommand command,
			VariableKind kind) {
		if (command.getArgumentCount() == 0) {
			return;
		}
		final TclArgument varName = command.getArgument(0);
		if (isSymbol(varName)) {
			final IVariable variable = new Variable();
			variable.setRange(command);
			variable.setNameRange(varName);
			variable.setName(asSymbol(varName));
			variable.setKind(kind);
			variable.setVisibility(clazz.peekVisibility());
			clazz.addMember(variable);
		}
	}

	/**
	 * @param clazz
	 * @param command
	 * @param kind
	 */
	protected void handleProc(IClass clazz, ICommand command, MethodKind kind) {
		if (command.getArgumentCount() == 0) {
			return;
		}
		final TclArgument procName = command.getArgument(0);
		if (isSymbol(procName)) {
			final IMethod method = new Method();
			method.setRange(command);
			method.setNameRange(procName);
			method.setName(asSymbol(procName));
			method.setKind(kind);
			method.setVisibility(clazz.peekVisibility());
			if (command.getArgumentCount() >= 2) {
				parseRawParameters(command.getArgument(1), method
						.getParameters());
			}
			if (command.getArgumentCount() == 3) {
				method.addBody(command.getArgument(2));
			}
			clazz.addMember(method);
		}
	}

	/**
	 * @param clazz
	 * @param command
	 */
	protected void handleDestructor(IClass clazz, ICommand command) {
		if (command.getArgumentCount() == 0) {
			return;
		}
		final IMethod method = new Method();
		method.setRange(command);
		method.setNameRange(command.getName());
		method.setName(asSymbol(command.getName()));
		method.setKind(MethodKind.DESTRUCTOR);
		method.setVisibility(clazz.peekVisibility());// FIXME check
		if (command.getArgumentCount() == 1) {
			method.addBody(command.getArgument(0));
		}
		clazz.addMember(method);
	}

	/**
	 * @param clazz
	 * @param command
	 */
	protected void handleConstructor(IClass clazz, ICommand command) {
		if (command.getArgumentCount() < 2) {
			return;
		}
		final IMethod method = new Method();
		method.setRange(command);
		method.setNameRange(command.getName());
		method.setName(asSymbol(command.getName()));
		method.setKind(MethodKind.CONSTRUCTOR);
		method.setVisibility(clazz.peekVisibility());// FIXME check
		parseRawParameters(command.getArgument(0), method.getParameters());
		method.addBody(command.getArgument(1));
		if (command.getArgumentCount() == 3) {
			method.addBody(command.getArgument(2));
		}
		clazz.addMember(method);
	}

	/**
	 * @param clazz
	 * @param command
	 * @param visibility
	 */
	protected void handleVisibility(IClass clazz, ICommand command,
			ITclModelBuildContext context, Visibility visibility) {
		if (command.getArgumentCount() == 0) {
			return;
		}
		clazz.pushVisibility(visibility);
		if (command.getArgumentCount() == 1) {
			TclArgument bodyArg = command.getArgument(0);
			processContent(clazz, context.parse(bodyArg).getCommands(), context);
		} else {
			processContent(clazz, new PrefixedCommandImpl(command), context);
		}
		clazz.popVisibility();
	}
}
