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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.ISourceElementRequestorExtension;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.core.ISourceElementParser;
import org.eclipse.dltk.core.ISourceModuleInfoCache.ISourceModuleInfo;
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.tcl.internal.parser.NewTclSourceParser;
import org.eclipse.dltk.tcl.internal.parser.TclSourceElementParser;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuilderDetector;
import org.eclipse.dltk.tcl.structure.TclProcessorUtil;

public class TclSourceElementParser2 extends TclSourceElementParser implements
		ISourceElementParser {

	private class TclModelBuilderVisitor extends TclVisitor {

		private final TclModelBuildContext context;

		/**
		 * @param context
		 */
		private TclModelBuilderVisitor(TclModelBuildContext context) {
			this.context = context;
		}

		@Override
		public boolean visit(TclCommand command) {
			final String commandName = TclProcessorUtil.asString(command
					.getName());
			context.resetAttributes();
			for (ITclModelBuilderDetector detector : detectors) {
				final String builderId = detector.detect(commandName, command,
						context);
				if (builderId != null) {
					final ITclModelBuilder builder = getBuilder(builderId, true);
					if (builder != null) {
						return builder.process(command, context);
					}
					return true;
				}
			}
			final ITclModelBuilder builder = getBuilder(commandName, false);
			if (builder != null) {
				return builder.process(command, context);
			}
			return true;
		}

		@Override
		public void endVisit(TclCommand command) {
			context.leave(command);
		}
	}

	public static final boolean DEBUG = true;

	public static boolean USE_NEW = DEBUG;

	@SuppressWarnings("deprecation")
	public static void refreshOptions() {
		USE_NEW = DEBUG
				&& !TclPlugin.getDefault().getPluginPreferences().getBoolean(
						TclSourceElementParser2.class.getName());
	}

	static {
		refreshOptions();
	}

	private boolean isStructureMode(ISourceElementRequestor requestor) {
		if (requestor instanceof ISourceElementRequestorExtension) {
			return ((ISourceElementRequestorExtension) requestor).getMode() == ISourceElementRequestorExtension.MODE_STRUCTURE;
		} else {
			return false;
		}
	}

	@Override
	public void parseSourceModule(
			org.eclipse.dltk.compiler.env.ISourceModule module,
			ISourceModuleInfo mifo) {
		final ISourceElementRequestor requestor = getRequestor();
		if (USE_NEW && isStructureMode(requestor)) {
			initDetectors();
			final IProblemReporter reporter = getProblemReporter();
			// TODO load from disk cache
			// TODO load from memory cache
			TclErrorCollector collector = (reporter != null) ? new TclErrorCollector()
					: null;
			final String source = module.getSourceContents();
			final TclModelBuildContext context = new TclModelBuildContext(this,
					requestor, collector, source);
			requestor.enterModule();
			//
			final TclParser newParser = new TclParser();
			final NamespaceScopeProcessor coreProcessor = DefinitionManager
					.getInstance().getCoreProcessor();
			TclModule tclModule = newParser.parseModule(source, context
					.getErrorReporter(), coreProcessor);
			traverse(tclModule.getStatements(), context);
			//
			requestor.exitModule(source.length());
			if (collector != null) {
				final ISourceLineTracker tracker = NewTclSourceParser
						.createLineTracker(tclModule);
				collector.reportAll(reporter, tracker);
			}
		} else {
			super.parseSourceModule(module, mifo);
		}
	}

	public void parse(ITclModelBuildContext context, String source, int offset) {
		final TclParser newParser = new TclParser();
		newParser.setGlobalOffset(offset);
		final NamespaceScopeProcessor coreProcessor = DefinitionManager
				.getInstance().getCoreProcessor();
		List<TclCommand> commands = newParser.parse(source, context
				.getErrorReporter(), coreProcessor);
		traverse(commands, (TclModelBuildContext) context);
	}

	protected void traverse(List<TclCommand> commands,
			TclModelBuildContext context) {
		TclParserUtils.traverse(commands, new TclModelBuilderVisitor(context));
	}

	private void initDetectors() {
		if (detectors == null) {
			detectors = ModelBuilderManager.getInstance().getDetectors();
		}
	}

	protected ITclModelBuilderDetector[] detectors = null;

	private Map<String, ITclModelBuilder> builders = new HashMap<String, ITclModelBuilder>();

	private static final ITclModelBuilder NULL_BUILDER = new ITclModelBuilder() {
		public boolean process(TclCommand command, ITclModelBuildContext context) {
			return true;
		}
	};

	protected ITclModelBuilder getBuilder(String id, boolean logMissingAsError) {
		ITclModelBuilder builder = builders.get(id);
		if (builder == null) {
			builder = ModelBuilderManager.getInstance().getModelBuilder(id);
			if (builder == null) {
				if (logMissingAsError) {
					TclPlugin.error("Tcl Model Builder '" + id
							+ "' is not found");
				}
				builder = NULL_BUILDER;
			}
			builders.put(id, builder);
		}
		return builder != NULL_BUILDER ? builder : null;
	}

}
