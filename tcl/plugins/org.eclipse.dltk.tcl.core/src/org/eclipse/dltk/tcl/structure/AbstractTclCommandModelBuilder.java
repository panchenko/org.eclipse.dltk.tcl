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
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.TclConstants;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.internal.core.codeassist.TclVisibilityUtils;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.printer.SimpleCodePrinter;
import org.eclipse.emf.common.util.EList;

/**
 * @since 2.0
 */
public abstract class AbstractTclCommandModelBuilder extends
		TclModelBuilderUtil implements ITclModelBuilder {

	protected static abstract class FieldInitializer {

		public abstract void initialize(FieldInfo info);

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
		processField(command, nameArg, varName, modifiers, context, null, null);
	}

	protected void processField(TclCommand command, TclArgument nameArg,
			String varName, final int modifiers, ITclModelBuildContext context,
			ITclTypeHandler exit, FieldInitializer initializer) {
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
		if (initializer != null) {
			initializer.initialize(fi);
		}
		String fullName = TclParseUtil.escapeName(varName);
		// TODO for (int i = 0; i < extensions.length; i++) {
		// if ((exit = extensions[i].processField(decl, this)) != null) {
		// continue;
		// }
		// }
		if (exit == null) {
			exit = context.get(ITclTypeResolver.class).resolveMemberType(fi,
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

	protected static class Parameter {
		final String name;
		final String defaultValue;
		final int start;
		final int end;

		public Parameter(TclArgument argument) {
			this.name = asSymbol(argument);
			this.start = argument.getStart();
			this.end = argument.getEnd();
			this.defaultValue = null;
		}

		public Parameter(String name, int start, int end) {
			this.name = name;
			this.start = start;
			this.end = end;
			this.defaultValue = null;
		}

		public Parameter(String name, int start, int end, String defaultValue) {
			this.name = name;
			this.start = start;
			this.end = end;
			this.defaultValue = defaultValue;
		}

		public String getName() {
			return name;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}

	}

	protected List<Parameter> parseParameters(TclArgument argument) {
		if (argument instanceof StringArgument) {
			return Collections.singletonList(new Parameter(argument));
		} else if (argument instanceof TclArgumentList) {
			final TclArgumentList list = (TclArgumentList) argument;
			final List<Parameter> parameters = new ArrayList<Parameter>(list
					.getArguments().size());
			for (TclArgument arg : list.getArguments()) {
				if (arg instanceof StringArgument) {
					parameters.add(new Parameter(arg));
				} else if (arg instanceof TclArgumentList) {
					final EList<TclArgument> argWithInitializer = ((TclArgumentList) arg)
							.getArguments();
					final TclArgument pName = argWithInitializer.get(0);
					if (argWithInitializer.size() >= 2) {
						parameters.add(new Parameter(asSymbol(pName), pName
								.getStart(), pName.getEnd(), TclProcessorUtil
								.asString(argWithInitializer.get(1))));
					} else if (argWithInitializer.size() == 1) {
						parameters.add(new Parameter(pName));
					} else {
						parameters.add(new Parameter(Util.EMPTY_STRING, arg
								.getStart(), arg.getEnd()));
					}
				} else {
					parameters.add(new Parameter(arg));
				}
			}
			return parameters;
		} else {
			return Collections.singletonList(new Parameter(argument));
		}
	}

	protected void parseRawParameters(TclArgument args,
			List<Parameter> parameters) {
		for (TclArgument a : toWords(args)) {
			if (a instanceof StringArgument) {
				String aa = ((StringArgument) a).getValue();
				if (aa.startsWith("{") && aa.endsWith("}")
						|| aa.startsWith("\"") && aa.endsWith("\"")) {
					List<TclArgument> parts = toWords(a);
					if (!parts.isEmpty()) {
						TclArgument first = parts.get(0);
						if (first instanceof StringArgument) {
							parameters.add(new Parameter(first));
						}
					}
				} else {
					parameters.add(new Parameter(a));
				}
			}
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

	private List<TclArgument> toWords(TclArgument argument) {
		int offset = argument.getStart();
		final String content;
		if (argument instanceof StringArgument) {
			final String value = ((StringArgument) argument).getValue();
			int len = value.length();
			if (len >= 2) {
				if (value.charAt(0) == '{' && value.charAt(len - 1) == '}') {
					content = value.substring(1, len - 1);
					++offset;
				} else if (value.charAt(0) == '"'
						&& value.charAt(len - 1) == '"') {
					content = value.substring(1, len - 1);
					++offset;
				} else {
					content = value;
				}
			} else {
				content = value;
			}
		} else {
			content = SimpleCodePrinter.getArgumentString(argument, argument
					.getStart());
		}
		TclParser parser = new TclParser();
		parser.setGlobalOffset(offset);
		List<TclCommand> commands = parser.parse(content);
		List<TclArgument> result = new ArrayList<TclArgument>();
		for (TclCommand c : commands) {
			result.add(c.getName());
			result.addAll(c.getArguments());
		}
		return result;
	}

}
