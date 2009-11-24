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
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.structure.ITclAttribute;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHanlder;
import org.eclipse.dltk.tcl.structure.TclTypeResolver;

public class TclModelBuildContext implements ITclModelBuildContext {

	private static class TopLevelNamespace implements ITclTypeHanlder {

		public String getNamespace() {
			return "::";
		}

		public void leave(ISourceElementRequestor requestor) {
			// empty
		}

	}

	private final TclSourceElementParser2 fParser;
	private final ISourceElementRequestor fRequestor;
	private final String source;
	private final ITclErrorReporter errorReporter;

	public TclModelBuildContext(TclSourceElementParser2 parser,
			ISourceElementRequestor requestor, ITclErrorReporter errorReporter,
			String source) {
		this.fParser = parser;
		this.fRequestor = requestor;
		this.errorReporter = errorReporter;
		this.source = source;
		this.exitStack.push(new TopLevelNamespace());
	}

	public ISourceElementRequestor getRequestor() {
		return fRequestor;
	}

	public CharSequence getSource() {
		return source;
	}

	public String substring(int beginIndex, int endIndex) {
		return source.substring(beginIndex, endIndex);
	}

	public ITclErrorReporter getErrorReporter() {
		return errorReporter;
	}

	private Stack<ITclTypeHanlder> exitStack = new Stack<ITclTypeHanlder>();

	public String getEnclosingNamespace() {
		for (int head = exitStack.size(); --head >= 0;) {
			ITclTypeHanlder handler = exitStack.get(head);
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
		if (handler instanceof ITclTypeHanlder) {
			exitStack.push((ITclTypeHanlder) handler);
		}
	}

	public void leave(TclCommand command) {
		final List<ITclModelHandler> commandHandlers = handlers.remove(command);
		if (commandHandlers != null) {
			for (ITclModelHandler handler : commandHandlers) {
				handler.leave(fRequestor);
				if (handler instanceof ITclTypeHanlder) {
					exitStack.remove(handler);
				}
			}
		}
	}

	private TclTypeResolver typeResolver = null;

	@SuppressWarnings("unchecked")
	public <E> E get(Class<E> clazz) {
		if (clazz == TclTypeResolver.class) {
			if (typeResolver == null) {
				typeResolver = new TclTypeResolver(fRequestor, this);
			}
			return (E) typeResolver;
		} else {
			return null;
		}
	}

	public void parse(String source, int offset) {
		final TclParser newParser = new TclParser();
		newParser.setGlobalOffset(offset);
		final NamespaceScopeProcessor coreProcessor = DefinitionManager
				.getInstance().getCoreProcessor();
		List<TclCommand> commands = newParser.parse(source, errorReporter,
				coreProcessor);
		fParser.traverse(commands, this);
	}

	private final List<ITclAttribute> attributes = new ArrayList<ITclAttribute>();

	public void addAttribute(ITclAttribute attribute) {
		attributes.add(attribute);
	}

	public void resetAttributes() {
		attributes.clear();
	}

}
