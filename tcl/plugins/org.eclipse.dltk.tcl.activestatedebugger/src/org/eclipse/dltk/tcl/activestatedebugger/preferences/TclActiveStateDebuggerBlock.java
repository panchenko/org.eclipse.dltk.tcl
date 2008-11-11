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
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.debug.ui.preferences.ExternalDebuggingEngineOptionsBlock;
import org.eclipse.dltk.tcl.activestatedebugger.ErrorAction;
import org.eclipse.dltk.tcl.activestatedebugger.InstrumentationFeature;
import org.eclipse.dltk.ui.environment.EnvironmentPathBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class TclActiveStateDebuggerBlock extends
		ExternalDebuggingEngineOptionsBlock {
	private EnvironmentPathBlock pdxPath;

	/**
	 * @param context
	 * @param project
	 * @param allKeys
	 * @param container
	 */
	public TclActiveStateDebuggerBlock(IStatusChangeListener context,
			IProject project, PreferenceKey[] allKeys,
			IWorkbenchPreferenceContainer container) {
		super(context, project, allKeys, container);
	}

	@Override
	protected Control createOptionsBlock(Composite parent) {
		final TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		final TabItem pathTab = new TabItem(tabFolder, SWT.NONE);
		pathTab.setText(PreferenceMessages.path_tab);
		pathTab.setControl(super.createOptionsBlock(tabFolder));
		final TabItem instrTab = new TabItem(tabFolder, SWT.NONE);
		instrTab.setText(PreferenceMessages.instrumentation_tab);
		instrTab.setControl(createInstrumentationPage(tabFolder));
		return tabFolder;
	}

	/**
	 * @return
	 */
	public static String[] createErrorActionItems() {
		final ErrorAction[] errorActions = ErrorAction.values();
		final String[] names = new String[errorActions.length + 1];
		names[0] = PreferenceMessages.instrumentation_errorAction_default_caption;
		for (int i = 0; i < errorActions.length; ++i) {
			names[i + 1] = errorActions[i].getValue();
		}
		return names;
	}

	private Button[] instrumentationButtons;
	private Combo errorActionCombo;
	private InstrumentationPatternList includes;
	private InstrumentationPatternList excludes;

	private Control createInstrumentationPage(Composite parent) {
		final Composite composite = SWTFactory.createComposite(parent, parent
				.getFont(), 1, 1, GridData.FILL);
		Group patterns = SWTFactory.createGroup(composite,
				PreferenceMessages.instrumentation_patternsGroup, 1, 1,
				GridData.FILL_BOTH);
		includes = new InstrumentationPatternList(patterns,
				PreferenceMessages.instrumentation_patterns_includeTitle,
				PreferenceMessages.instrumentation_patterns_includeMessage);
		excludes = new InstrumentationPatternList(patterns,
				PreferenceMessages.instrumentation_patterns_excludeTitle,
				PreferenceMessages.instrumentation_patterns_excludeMessage);
		Group options = SWTFactory.createGroup(composite,
				PreferenceMessages.instrumentation_options, 2, 1,
				GridData.FILL_HORIZONTAL);
		((GridLayout) options.getLayout()).makeColumnsEqualWidth = true;
		SWTFactory.createLabel(options,
				PreferenceMessages.instrumentation_errorAction_label, 1);
		errorActionCombo = SWTFactory.createCombo(options, SWT.READ_ONLY, 1,
				createErrorActionItems());
		InstrumentationFeature[] features = InstrumentationFeature.values();
		instrumentationButtons = new Button[features.length];
		for (int i = 0; i < features.length; ++i) {
			instrumentationButtons[i] = SWTFactory.createCheckButton(options,
					features[i].getCaption());
		}

		// TODO Auto-generated method stub
		return composite;
	}

	@Override
	protected void initialize() {
		super.initialize();
		// patterns
		includes
				.setInput(getString(TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_INCLUDE));
		excludes
				.setInput(getString(TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_EXCLUDE));
		// instrumentation features
		Set<InstrumentationFeature> result = InstrumentationFeature
				.decode(getString(TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_FEATURES));
		InstrumentationFeature[] features = InstrumentationFeature.values();
		for (int i = 0; i < features.length; ++i) {
			instrumentationButtons[i]
					.setSelection(result.contains(features[i]));
		}
		// error action
		final ErrorAction errorAction = ErrorAction
				.decode(getString(TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_ERROR_ACTION));
		final int errorActionIndex;
		if (errorAction == null) {
			errorActionIndex = 0;
		} else {
			errorActionIndex = Arrays.asList(ErrorAction.values()).indexOf(
					errorAction) + 1;
		}
		errorActionCombo.select(errorActionIndex);
	}

	protected void createEngineBlock(Composite parent) {
		super.createEngineBlock(parent);
		createPDXGroup(parent);
	}

	protected void createOtherBlock(Composite parent) {
		addDownloadLink(parent, PreferenceMessages.DebuggingEngineDownloadPage,
				PreferenceMessages.DebuggingEngineDownloadPageLink);
	}

	private void createPDXGroup(final Composite parent) {

		final Group group = SWTFactory.createGroup(parent,
				PreferenceMessages.DebuggingEnginePDXGroup, 3, 1,
				GridData.FILL_BOTH);
		pdxPath = new EnvironmentPathBlock(true);
		pdxPath.createControl(group, getRelevantEnvironments());
		Map paths = EnvironmentPathUtils
				.decodePaths(getString(TclActiveStateDebuggerPreferencePage.PDX_PATH));
		pdxPath.setPaths(paths);
	}

	protected boolean processChanges(IWorkbenchPreferenceContainer container) {
		// PDX paths
		String pdxPathKeyValue = EnvironmentPathUtils.encodePaths(pdxPath
				.getPaths());
		setString(TclActiveStateDebuggerPreferencePage.PDX_PATH,
				pdxPathKeyValue);
		// patterns
		setString(TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_INCLUDE,
				includes.getValue());
		setString(TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_EXCLUDE,
				excludes.getValue());
		// Instrumentation features
		Set<InstrumentationFeature> selectedFeatures = new HashSet<InstrumentationFeature>();
		InstrumentationFeature[] features = InstrumentationFeature.values();
		for (int i = 0; i < features.length; ++i) {
			if (instrumentationButtons[i].getSelection()) {
				selectedFeatures.add(features[i]);
			}
		}
		setString(
				TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_FEATURES,
				InstrumentationFeature.encode(selectedFeatures));
		// Error action
		final int errorActionIndex = errorActionCombo.getSelectionIndex();
		final ErrorAction[] errorActions = ErrorAction.values();
		if (errorActionIndex > 0 && errorActionIndex <= errorActions.length) {
			setString(
					TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_ERROR_ACTION,
					errorActions[errorActionIndex - 1].name());
		} else {
			setString(
					TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_ERROR_ACTION,
					Util.EMPTY_STRING);
		}
		// super
		return super.processChanges(container);
	}

	protected PreferenceKey getDebuggingEnginePathKey() {
		return TclActiveStateDebuggerPreferencePage.ENGINE_PATH;
	}

	protected PreferenceKey getLogFileNamePreferenceKey() {
		return TclActiveStateDebuggerPreferencePage.LOG_FILE_NAME;
	}

}
