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
package org.eclipse.dltk.tcl.internal.core.packages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.tcl.ast.AstFactory;
import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.parser.printer.SimpleCodePrinter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @since 2.0
 */
public class TclVariableResolver {

	public static interface IVariableRegistry {
		/**
		 * Returns value for the specified variable or <code>null</code> if not
		 * defined
		 * 
		 * @param name
		 * @return
		 */
		String getValue(String name, String index);
	}

	public static class SimpleVariableRegistry implements IVariableRegistry {

		private final Map<String, Object> values;

		/**
		 * Accept String or Map<String, String> values
		 * 
		 * @param values
		 */
		public SimpleVariableRegistry(Map<String, Object> values) {
			this.values = values;
		}

		@SuppressWarnings("unchecked")
		public String getValue(String name, String index) {
			Object value = values.get(name);
			if (value instanceof Map) {
				if (index == null) {
					index = "";
				}
				Map<String, String> map = (Map<String, String>) value;
				return map.get(index);
			}
			return (String) value;
		}

	}

	private final IVariableRegistry registry;

	public TclVariableResolver(IVariableRegistry registry) {
		this.registry = registry;
	}

	public String resolve(String value) {
		TclParser parser = new TclParser();
		TclErrorCollector collector = new TclErrorCollector();
		List<TclCommand> result = parser.parse(value, collector, null);
		if (collector.getCount() > 0) {
			return value;
		}
		final List<VariableReference> variables = new ArrayList<VariableReference>();
		TclParserUtils.traverse(result, new TclVisitor() {
			@Override
			public boolean visit(VariableReference list) {
				variables.add(list);
				return super.visit(list);
			}

			// Skip substitutions
			@Override
			public boolean visit(Substitution substitution) {
				return false;
			}
		});
		boolean hasModifications = false;
		for (VariableReference variableReference : variables) {
			EObject container = variableReference.eContainer();
			if (container == null) {
				continue;
			}
			String name = variableReference.getName();
			TclArgument index = variableReference.getIndex();
			String indexValue = null;
			if (index instanceof StringArgument) {
				indexValue = ((StringArgument) index).getValue();
			} else if (index != null) {
				indexValue = resolve(SimpleCodePrinter.getArgumentString(index,
						0, false));
			}
			String resultValue = registry.getValue(name, indexValue);
			// If has unresolved value then return null
			if (resultValue == null) {
				return null;
			}
			StringArgument string = AstFactory.eINSTANCE.createStringArgument();
			string.setValue(resultValue);
			string.setStart(variableReference.getStart());
			string.setEnd(variableReference.getEnd());
			// Replace parent with StringArgument
			if (container instanceof TclCommand) {
				TclCommand cmd = (TclCommand) container;
				EList<TclArgument> args = cmd.getArguments();
				if (args.contains(variableReference)) {
					args.set(args.indexOf(variableReference), string);
				} else {
					cmd.setName(string);
				}
				hasModifications = true;
			} else if (container instanceof TclArgumentList) {
				TclArgumentList cmd = (TclArgumentList) container;
				EList<TclArgument> args = cmd.getArguments();
				args.set(args.indexOf(variableReference), string);
				hasModifications = true;
			} else if (container instanceof ComplexString) {
				ComplexString cmd = (ComplexString) container;
				EList<TclArgument> args = cmd.getArguments();
				args.set(args.indexOf(variableReference), string);
				hasModifications = true;
			}
		}
		if (hasModifications) {
			String resultString = SimpleCodePrinter.getCommandsString(result,
					false).trim();
			return resultString;
		}
		return value;
	}

	public static String[] extractVariableNames(String value) {
		TclParser parser = new TclParser();
		TclErrorCollector collector = new TclErrorCollector();
		List<TclCommand> result = parser.parse(value, collector, null);
		if (collector.getCount() > 0) {
			return null;
		}
		final List<String> variables = new ArrayList<String>();
		TclParserUtils.traverse(result, new TclVisitor() {
			@Override
			public boolean visit(VariableReference list) {
				variables.add(list.getName());
				return super.visit(list);
			}

			// Skip substitutions
			@Override
			public boolean visit(Substitution substitution) {
				return false;
			}
		});
		if (variables.isEmpty()) {
			return null;
		}
		return variables.toArray(new String[variables.size()]);
	}
}
