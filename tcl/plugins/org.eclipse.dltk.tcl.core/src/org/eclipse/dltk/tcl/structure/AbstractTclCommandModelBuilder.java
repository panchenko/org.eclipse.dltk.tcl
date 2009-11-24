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
package org.eclipse.dltk.tcl.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.IElementRequestor.FieldInfo;
import org.eclipse.dltk.compiler.IElementRequestor.MethodInfo;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.ast.Node;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.TclConstants;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.internal.core.codeassist.TclVisibilityUtils;
import org.eclipse.dltk.tcl.internal.structure.ITclTypeHanlder;
import org.eclipse.dltk.tcl.internal.structure.TclProcessorUtil;
import org.eclipse.dltk.tcl.internal.structure.TclTypeResolver;
import org.eclipse.emf.common.util.EList;

/**
 * @since 2.0
 */
public abstract class AbstractTclCommandModelBuilder implements
		ITclModelBuilder {

	/**
	 * @param argument
	 * @return
	 */
	protected static boolean isLevel(TclArgument argument) {
		final String value = TclProcessorUtil.asString(argument);
		if (value.length() == 0) {
			return false;
		}
		if (value.charAt(0) == '#') {
			return isNumber(value, 1);
		} else {
			return isNumber(value, 0);
		}
	}

	/**
	 * @param value
	 * @param beginIndex
	 * @return
	 */
	protected static boolean isNumber(String value, int beginIndex) {
		if (beginIndex < value.length()) {
			for (int i = beginIndex, len = value.length(); i < len; ++i) {
				if (!Character.isDigit(value.charAt(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param node
	 * @param message
	 * @param severity
	 */
	protected void report(ITclModelBuildContext context, Node node,
			String message, int severity) {
		// TODO Auto-generated method stub

	}

	protected void processField(TclCommand command, TclArgument nameArg,
			String varName, final int modifiers, ITclModelBuildContext context) {
		if (varName == null) {
			varName = asSymbol(nameArg);
		}
		FieldInfo fi = new FieldInfo();
		fi.nameSourceStart = nameArg.getStart();
		fi.nameSourceEnd = nameArg.getEnd() - 1;
		fi.declarationStart = fi.nameSourceStart;
		fi.modifiers = modifiers;

		String arrayIndex = null;
		if (TclParseUtil.isArrayVariable(varName)) {
			arrayIndex = TclParseUtil.extractArrayIndex(varName);
			varName = TclParseUtil.extractArrayName(varName);
		}
		fi.name = varName;
		String fullName = TclParseUtil.escapeName(varName);
		ITclTypeHanlder exit = null;
		// TODO for (int i = 0; i < extensions.length; i++) {
		// if ((exit = extensions[i].processField(decl, this)) != null) {
		// continue;
		// }
		// }
		if (exit == null) {
			exit = context.get(TclTypeResolver.class).resolveType(fi,
					command.getEnd(), fullName);
		}

		boolean needExit = context.getRequestor().enterFieldCheckDuplicates(fi);

		if (needExit) {
			if (arrayIndex != null) {
				ISourceElementRequestor.FieldInfo fiIndex = new ISourceElementRequestor.FieldInfo();
				fiIndex.name = varName + "(" + arrayIndex + ")";
				fiIndex.nameSourceStart = fi.nameSourceStart;
				fiIndex.nameSourceEnd = fi.nameSourceEnd;
				fiIndex.declarationStart = fi.declarationStart;
				fiIndex.modifiers = TclConstants.TCL_FIELD_TYPE_INDEX
						| fi.modifiers;
				if (context.getRequestor().enterFieldCheckDuplicates(fiIndex)) {
					context.getRequestor().exitField(command.getEnd());
				}
			}
			context.getRequestor().exitField(command.getEnd());
		}
		exit.leave(context.getRequestor());
	}

	protected void processField(TclCommand command, final TclArgument nameArg,
			ITclModelBuildContext context) {
		if (!isSymbol(nameArg)) {
			return;
		}
		final String varName = asSymbol(nameArg);
		final int modifiers = TclVisibilityUtils.isPrivate(varName) ? Modifiers.AccPrivate
				: Modifiers.AccPublic;
		processField(command, nameArg, varName, modifiers, context);
	}

	protected static boolean isSymbol(TclArgument argument) {
		return !(argument instanceof Substitution);
	}

	protected static String asSymbol(final TclArgument nameArg) {
		return TclProcessorUtil.asString(nameArg);
		// TODO Check TclParseUtil.makeVariable()
	}

	protected static class Parameter {
		final String name;
		final String defaultValue;

		public Parameter(String name) {
			this(name, null);
		}

		public Parameter(String name, String defaultValue) {
			this.name = name;
			this.defaultValue = defaultValue;
		}
	}

	protected List<Parameter> parseParameters(TclArgument argument) {
		if (argument instanceof StringArgument) {
			return Collections.singletonList(new Parameter(
					((StringArgument) argument).getValue()));
		} else if (argument instanceof TclArgumentList) {
			final TclArgumentList list = (TclArgumentList) argument;
			final List<Parameter> parameters = new ArrayList<Parameter>(list
					.getArguments().size());
			for (TclArgument arg : list.getArguments()) {
				if (arg instanceof StringArgument) {
					parameters.add(new Parameter(((StringArgument) arg)
							.getValue()));
				} else if (arg instanceof TclArgumentList) {
					final EList<TclArgument> argWithInitializer = ((TclArgumentList) arg)
							.getArguments();
					if (argWithInitializer.size() >= 2) {
						parameters.add(new Parameter(
								asSymbol(argWithInitializer.get(0)),
								TclProcessorUtil.asString(argWithInitializer
										.get(1))));
					} else if (argWithInitializer.size() == 1) {
						parameters.add(new Parameter(
								asSymbol(argWithInitializer.get(0))));
					} else {
						parameters.add(new Parameter(Util.EMPTY_STRING));
					}
				} else {
					parameters.add(new Parameter(asSymbol(arg)));
				}
			}
			return parameters;
		} else {
			return Collections.singletonList(new Parameter(asSymbol(argument)));
		}
	}

	protected void fillParameters(MethodInfo mi, List<Parameter> parameters) {
		if (parameters.isEmpty()) {
			return;
		}
		mi.parameterNames = new String[parameters.size()];
		mi.parameterInitializers = new String[parameters.size()];
		for (int i = 0; i < parameters.size(); ++i) {
			Parameter parameter = parameters.get(i);
			mi.parameterNames[i] = parameter.name;
			mi.parameterInitializers[i] = parameter.defaultValue;
		}
	}

}
