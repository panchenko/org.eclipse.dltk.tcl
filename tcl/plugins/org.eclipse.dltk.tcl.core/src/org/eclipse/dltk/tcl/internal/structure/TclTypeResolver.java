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

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.IElementRequestor.ElementInfo;
import org.eclipse.dltk.compiler.IElementRequestor.TypeInfo;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext.ITclModelHandler;

public class TclTypeResolver {

	private final ISourceElementRequestor fRequestor;
	private final TclModelBuildContext fContext;

	public TclTypeResolver(ISourceElementRequestor fRequestor,
			TclModelBuildContext fContext) {
		this.fRequestor = fRequestor;
		this.fContext = fContext;
	}

	private static class ExitFromType implements ITclModelHandler,
			ITclTypeHanlder {
		private int level;
		private int end;
		private boolean exitFromModule;
		private String namespace;

		// public boolean created = false;

		public ExitFromType(int level, int declEnd, boolean mod, String pop) {
			this(level, declEnd, mod, pop, false);
		}

		public ExitFromType(int level, int declEnd, boolean mod, String pop,
				boolean created) {
			this.level = level;
			this.end = declEnd;
			this.exitFromModule = mod;
			this.namespace = pop;
			// this.created = created;
		}

		public void leave(ISourceElementRequestor requestor) {
			for (int i = 0; i < this.level; i++) {
				requestor.exitType(this.end);
			}
			if (this.exitFromModule) {
				requestor.exitModuleRoot();
			}
		}

		public String getNamespace() {
			return namespace;
		}
	}

	private static String removeLastSegment(String s, String delimeter) {
		if (s.indexOf("::") == -1) {
			return Util.EMPTY_STRING;
		}
		int pos = s.length() - 1;
		while (s.charAt(pos) != ':') {
			pos--;
		}
		if (pos > 1) {
			return s.substring(0, pos - 1);
		} else {
			return "::";
		}
	}

	/**
	 * Enters into required type (if type doesn't exists, creates it). If name
	 * is fully-qualified (starting with a "::") then it is always resolved
	 * globally. Else search are done first in current namespace, than in
	 * global. Flags <code>onlyCurrent</code> allows to search
	 * <em>not qualified</em> names only in current namespace. If type doesn't
	 * exists, it will be created. If name is qualified, it will be created
	 * globally, else in current namespace.s
	 * 
	 * @param decl
	 *            expression containing typedeclaration correct source ranges
	 *            setup
	 * @param name
	 *            name containing a type
	 * @return ExitFromType object, that should be called to exit
	 */
	public ITclTypeHanlder resolveType(ElementInfo decl, int sourceEnd,
			String name) {
		String type = removeLastSegment(name, "::");
		while (type.length() > 2 && type.endsWith("::")) {
			type = type.substring(0, type.length() - 2);
		}

		if (type.length() == 0) {
			return new ExitFromType(0, 0, false, null);
		}

		if (type.equals("::")) {
			this.fRequestor.enterModuleRoot();
			return new ExitFromType(0, sourceEnd, true, "::");
		}

		boolean fqn = type.startsWith("::");

		String fullyQualified = type;
		if (!fqn) { // make name fully-qualified
			String e = fContext.getEnclosingNamespace();
			if (e == null) {
				throw new AssertionError("there are no enclosing namespace!");
			}
			if (!e.endsWith("::")) {
				e += "::";
			}
			fullyQualified = e + type;
		}

		// first, try existent
		if (this.fRequestor.enterTypeAppend(type, "::")) {
			return new ExitFromType(1/* split.length */, sourceEnd, false,
					fullyQualified);
		}

		// create it
		// Lets add warning in any case.
		int needEnterLeave = 0;
		String[] split = null;
		String e = fContext.getEnclosingNamespace();
		if (e == null) {
			throw new AssertionError("there are no enclosing namespace!");
		}
		boolean entered = false;
		boolean exitFromModule = false;
		if (e.length() > 0 && !fqn) {
			// We need to report warning here.
			entered = this.fRequestor.enterTypeAppend(e, "::");
		}
		if (fqn || !entered) {
			split = TclParseUtil.tclSplit(fullyQualified.substring(2));
			fRequestor.enterModuleRoot();
			exitFromModule = true;
		} else {
			if (!entered) {
				throw new AssertionError("can't enter to enclosing namespace!");
			}
			needEnterLeave++;
			split = TclParseUtil.tclSplit(type);
		}

		for (int i = 0; i < split.length; ++i) {
			if (split[i].length() > 0) {
				needEnterLeave++;
				if (!fRequestor.enterTypeAppend(split[i], "::")) {
					TypeInfo ti = new TypeInfo();
					if (decl instanceof TypeInfo) {
						ti.modifiers = ((TypeInfo) decl).modifiers;
					} else {
						ti.modifiers = Modifiers.AccNameSpace;
					}

					ti.name = split[i];
					ti.nameSourceStart = decl.nameSourceStart;
					ti.nameSourceEnd = decl.nameSourceEnd;
					ti.declarationStart = decl.declarationStart;
					if (decl instanceof TypeInfo) {
						ti.superclasses = ((TypeInfo) decl).superclasses;
					}
					this.fRequestor.enterType(ti);
				}
			}
		}
		return new ExitFromType(needEnterLeave, sourceEnd, exitFromModule,
				fullyQualified, true);
	}

}
