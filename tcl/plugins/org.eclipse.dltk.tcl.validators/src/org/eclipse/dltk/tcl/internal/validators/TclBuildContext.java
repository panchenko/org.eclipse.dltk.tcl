/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.validators;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.problem.ProblemCollector;
import org.eclipse.dltk.core.ISourceModuleInfoCache;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.core.ISourceModuleInfoCache.ISourceModuleInfo;
import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.dltk.core.caching.IContentCache;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.ast.TclModuleDeclaration;
import org.eclipse.dltk.tcl.internal.core.TclASTCache;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;

public class TclBuildContext {

	private static final String NEW_AST = "new_ast"; //$NON-NLS-1$
	private static final String NEW_PROBLEMS = "new_ast_problems"; //$NON-NLS-1$

	private static final String PROBLEMS_REPORTED = "problemsReported"; //$NON-NLS-1$

	private static boolean isReported(IBuildContext context) {
		return context.get(PROBLEMS_REPORTED) != null;
	}

	private static void setReported(IBuildContext context) {
		context.set(PROBLEMS_REPORTED, Boolean.TRUE);
	}

	public static TclModule getStatements(IBuildContext context) {
		Object object = context.get(NEW_AST);
		if (object != null && object instanceof TclModule) {
			if (!isReported(context)) {
				Object object2 = context.get(NEW_PROBLEMS);
				if (object2 instanceof ProblemCollector) {
					ProblemCollector collector = (ProblemCollector) object2;
					collector.copyTo(context.getProblemReporter());
				}
				setReported(context);
			}
			return (TclModule) object;
		}
		// Parse and store statements here.
		ISourceModuleInfoCache infoCache = ModelManager.getModelManager()
				.getSourceModuleInfoCache();
		ISourceModuleInfo info = infoCache.get(context.getSourceModule());
		ProblemCollector collector = new ProblemCollector();
		ModuleDeclaration cache = SourceParserUtil.getModuleFromCache(info, 0,
				collector);
		if (cache instanceof TclModuleDeclaration) {
			TclModuleDeclaration decl = (TclModuleDeclaration) cache;
			TclModule tclModule = decl.getTclModule();
			if (tclModule != null) {
				if (!isReported(context)) {
					collector.copyTo(context.getProblemReporter());
					setReported(context);
				}
				return tclModule;
			}
		}
		// Try to load module info from disk cache directly.
		IFileHandle handle = EnvironmentPathUtils.getFile(context
				.getSourceModule());
		if (handle != null) {
			IContentCache coreCache = ModelManager.getModelManager()
					.getCoreCache();
			TclModule module = TclASTCache.restoreTclModuleFromCache(handle,
					coreCache, collector);
			if (!isReported(context)) {
				collector.copyTo(context.getProblemReporter());
				setReported(context);
			}
			if (module != null) {
				context.set(NEW_AST, module);
				context.set(NEW_PROBLEMS, collector);
				// Save also to memory cache.
				return module;
			}
		}
		TclParser parser = new TclParser();
		TclErrorCollector tclCollector = new TclErrorCollector();
		TclModule module = parser.parseModule(
				new String(context.getContents()), tclCollector,
				DefinitionManager.getInstance().getCoreProcessor());
		if (!isReported(context)) {
			tclCollector.reportAll(context.getProblemReporter(), context
					.getLineTracker());
			setReported(context);
		}

		return module;
	}
}
