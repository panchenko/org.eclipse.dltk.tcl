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
package org.eclipse.dltk.tcl.internal.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.tcl.ast.AstFactory;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHandler;
import org.eclipse.dltk.tcl.structure.ITclTypeResolver;
import org.eclipse.dltk.tcl.structure.TclProcessorUtil;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class TclModelBuildContext implements ITclModelBuildContext {

	private static class TopLevelNamespace implements ITclTypeHandler {

		public String getNamespace() {
			return "::";
		}

		public void leave(ISourceElementRequestor requestor) {
			// empty
		}

	}

	private final TclSourceElementParser2 fParser;
	private final ISourceElementRequestor fRequestor;
	private final ITclErrorReporter errorReporter;

	public TclModelBuildContext(TclSourceElementParser2 parser,
			ISourceElementRequestor requestor, ITclErrorReporter errorReporter) {
		this.fParser = parser;
		this.fRequestor = requestor;
		this.errorReporter = errorReporter;
		this.exitStack.push(new TopLevelNamespace());
	}

	public ISourceElementRequestor getRequestor() {
		return fRequestor;
	}

	public ITclErrorReporter getErrorReporter() {
		return errorReporter;
	}

	private Stack<ITclTypeHandler> exitStack = new Stack<ITclTypeHandler>();

	public String getEnclosingNamespace() {
		for (int head = exitStack.size(); --head >= 0;) {
			ITclTypeHandler handler = exitStack.get(head);
			final String namespace = handler.getNamespace();
			if (namespace != null) {
				return namespace;
			}
		}
		return null;
	}

	private Map<TclCommand, List<ITclModelHandler>> handlers = new IdentityHashMap<TclCommand, List<ITclModelHandler>>();

	public void addHandler(TclCommand command, ITclModelHandler handler) {
		List<ITclModelHandler> commandHandlers = handlers.get(command);
		if (commandHandlers == null) {
			commandHandlers = new ArrayList<ITclModelHandler>(4);
			handlers.put(command, commandHandlers);
		}
		commandHandlers.add(handler);
		if (handler instanceof ITclTypeHandler) {
			exitStack.push((ITclTypeHandler) handler);
		}
	}

	public void leave(TclCommand command) {
		final List<ITclModelHandler> commandHandlers = handlers.remove(command);
		if (commandHandlers != null) {
			for (ITclModelHandler handler : commandHandlers) {
				handler.leave(fRequestor);
				if (handler instanceof ITclTypeHandler) {
					exitStack.remove(handler);
				}
			}
		}
	}

	public void enterNamespace(ITclTypeHandler typeHandler) {
		exitStack.push(typeHandler);
	}

	public void leaveNamespace(ITclTypeHandler namespace) {
		if (exitStack.remove(namespace)) {
			namespace.leave(fRequestor);
		}
	}

	private TclTypeResolver typeResolver = null;

	@SuppressWarnings("unchecked")
	public <E> E get(Class<E> clazz) {
		if (clazz == ITclTypeResolver.class) {
			if (typeResolver == null) {
				typeResolver = new TclTypeResolver(fRequestor, this);
			}
			return (E) typeResolver;
		} else {
			return null;
		}
	}

	public List<TclCommand> parse(String source, int offset) {
		return parse(source, offset, 0);
	}

	public List<TclCommand> parse(String source, int offset, int options) {
		final TclParser newParser = new TclParser();
		newParser.setGlobalOffset(offset);
		final NamespaceScopeProcessor coreProcessor = DefinitionManager
				.getInstance().createProcessor();
		List<TclCommand> commands = newParser.parse(source, errorReporter,
				coreProcessor);
		if ((options & NO_TRAVERSE) == 0) {
			fParser.traverse(commands, this);
		}
		return commands;
	}

	public Script parse(TclArgument arg) {
		return parse(arg, 0);
	}

	public Script parse(TclArgument arg, int options) {
		if (arg instanceof Script) {
			final Script script = (Script) arg;
			if ((options & NO_TRAVERSE) == 0) {
				fParser.traverse(script.getCommands(), this);
			}
			return script;
		} else {
			final Script script = AstFactory.eINSTANCE.createScript();
			script.setStart(arg.getStart());
			script.setEnd(arg.getEnd());
			final ITclParserInput input = TclProcessorUtil.asInput(arg);
			if (input != null) {
				script.setContentStart(input.getStart());
				script.setContentEnd(input.getEnd());
				script.getCommands().addAll(
						parse(input.getContent(), input.getStart(), options));
			} else {
				script.setContentStart(arg.getStart());
				script.setContentEnd(arg.getEnd());
			}
			EcoreUtil.replace(arg, script);
			return script;
		}
	}

	private final Map<String, Object> attributes = new HashMap<String, Object>();

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

}
