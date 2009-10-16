/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.core.search;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IParent;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchPattern;
import org.eclipse.dltk.core.search.SearchRequestor;
import org.eclipse.dltk.core.search.matching.MatchLocator;
import org.eclipse.dltk.internal.core.BuiltinSourceModule;
import org.eclipse.dltk.internal.core.ExternalSourceModule;
import org.eclipse.dltk.internal.core.Openable;
import org.eclipse.dltk.internal.core.SourceModule;
import org.eclipse.dltk.tcl.core.TclMatchLocatorParser;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.core.extensions.IMatchLocatorExtension;
import org.eclipse.dltk.tcl.internal.core.TclExtensionManager;

public class TclMatchLocator extends MatchLocator {
	IMatchLocatorExtension[] extensions = null;

	public TclMatchLocator(SearchPattern pattern, SearchRequestor requestor,
			IDLTKSearchScope scope, IProgressMonitor progressMonitor) {
		super(pattern, requestor, scope, progressMonitor);
		extensions = TclExtensionManager.getDefault()
				.getMatchLocatorExtensions();
	}

	/*
	 * Create method handle. Store occurences for create handle to retrieve
	 * possible duplicate ones.
	 */
	public IModelElement createMethodHandle(ISourceModule module,
			String methodName) {
		IMethod methodHandle = null;

		if (methodName.indexOf("::") != -1) {
			int pos = methodName.lastIndexOf("::");
			String cName = methodName.substring(0, pos);
			String name = methodName.substring(pos + 2);

			if (!cName.startsWith("$")) {
				cName = "$" + cName;
			}

			cName = cName.replaceAll("::", "\\$");
			if (!cName.equals("$")) {
				IType type = null;
				try {
					type = findTypeFrom(module.getChildren(), "", cName, '$');
				} catch (ModelException e) {
					if (DLTKCore.DEBUG) {
						e.printStackTrace();
					}
				}
				if (type != null) {
					methodHandle = type.getMethod(name);
				}
			} else {
				methodHandle = module.getMethod(methodName);
			}
		} else {
			methodHandle = module.getMethod(methodName);
		}

		resolveDuplicates(methodHandle);
		return methodHandle;
	}

	/**
	 * Creates an IMethod from the given method declaration and type.
	 */
	protected IModelElement createHandle(MethodDeclaration method,
			IModelElement parent) {
		// if (!(parent instanceof IType)) return parent;
		if (parent instanceof IType) {
			IType type = (IType) parent;
			return createMethodHandle(type, ((TclMatchLocatorParser) parser)
					.getRealMethodName(method));
		} else if (parent instanceof ISourceModule) {
			for (int i = 0; i < extensions.length; i++) {
				IModelElement handle = extensions[i].createMethodHandle(
						(ISourceModule) parent, method, this);
				if (handle != null) {
					return handle;
				}
			}
			if (method.getName().indexOf("::") != -1) {
				String methodName = method.getDeclaringTypeName()
						+ "::"
						+ ((TclMatchLocatorParser) parser)
								.getRealMethodName(method);
				return createMethodHandle((ISourceModule) parent, methodName);
			} else {
				return createMethodHandle((ISourceModule) parent, method
						.getName());
			}
		}
		return null;
	}

	protected IModelElement createTypeHandle(IType parent, String name) {
		if (name.indexOf("::") != -1) {
			String[] split = TclParseUtil.tclSplit(name);
			IType e = parent;
			for (int i = 0; i < split.length; i++) {
				e = e.getType(split[i]);
				if (e == null) {
					return null;
				}
			}
			if (e != null) {
				return e;
			}
		}
		return parent.getType(name);
	}

	protected IType createTypeHandle(String name) {
		Openable openable = this.currentPossibleMatch.openable;
		if (name.startsWith("::")) {
			name = name.substring(2);
		}
		if (name.endsWith("::")) {
			name = name.substring(0, name.length() - 2);
		}
		if (openable instanceof SourceModule
				|| openable instanceof ExternalSourceModule
				|| openable instanceof BuiltinSourceModule) {
			IParent e = ((IParent) openable);
			if (name.indexOf("::") != -1) {
				String[] split = TclParseUtil.tclSplit(name);
				for (int i = 0; i < split.length; i++) {
					if (e instanceof ISourceModule) {
						e = ((ISourceModule) e).getType(split[i]);
					} else if (e instanceof IType) {
						e = ((IType) e).getType(split[i]);
					} else {
						e = null;
					}
					if (e == null) {
						return null;
					}
				}
				if (e != null && e instanceof IType) {
					return (IType) e;
				}
			}
		}
		// return super.createTypeHandle(name);
		IType type = null;
		if (openable instanceof ISourceModule) {
			type = ((ISourceModule) openable).getType(name);
		}
		return type;
	}
}
