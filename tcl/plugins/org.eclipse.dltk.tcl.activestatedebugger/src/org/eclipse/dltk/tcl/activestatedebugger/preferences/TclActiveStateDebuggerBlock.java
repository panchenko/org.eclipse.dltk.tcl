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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.debug.ui.preferences.ExternalDebuggingEngineOptionsBlock;
import org.eclipse.dltk.debug.ui.preferences.ScriptDebugPreferencesMessages;
import org.eclipse.dltk.tcl.activestatedebugger.ErrorAction;
import org.eclipse.dltk.tcl.activestatedebugger.InstrumentationFeature;
import org.eclipse.dltk.ui.environment.EnvironmentPathBlock;
import org.eclipse.dltk.ui.preferences.IPreferenceChangeRebuildPrompt;
import org.eclipse.dltk.ui.preferences.PreferenceChangeRebuildPrompt;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
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

	private TclSpawnpointPreferenceBlock spawnpointBlock;

	@Override
	protected Control createOptionsBlock(Composite parent) {
		final TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		final TabItem pathTab = new TabItem(tabFolder, SWT.NONE);
		pathTab.setText(PreferenceMessages.path_tab);
		pathTab.setControl(super.createOptionsBlock(tabFolder));
		final TabItem instrTab = new TabItem(tabFolder, SWT.NONE);
		instrTab.setText(PreferenceMessages.instrumentation_tab);
		instrTab.setControl(createInstrumentationPage(tabFolder));
		final TabItem spawnpointTab = new TabItem(tabFolder, SWT.NONE);
		spawnpointTab.setText(PreferenceMessages.spawnpoints_tab);
		spawnpointBlock = new TclSpawnpointPreferenceBlock(
				new IShellProvider() {
					public Shell getShell() {
						return TclActiveStateDebuggerBlock.this.getShell();
					}
				}, this);
		spawnpointTab.setControl(spawnpointBlock.createControl(tabFolder));
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
			names[i + 1] = errorActions[i].getCaption();
		}
		return names;
	}

	private List<Button> instrumentationButtons;
	private Combo errorActionCombo;
	private InstrumentationPatternList patterns;

	private Control createInstrumentationPage(Composite parent) {
		final Composite composite = SWTFactory.createComposite(parent, parent
				.getFont(), 1, 1, GridData.FILL);
		Group groupPatterns = SWTFactory.createGroup(composite,
				PreferenceMessages.instrumentation_patternsGroup, 1, 1,
				GridData.FILL_BOTH);
		if (isProjectPreferencePage()) {
			patterns = new InstrumentationPatternList(getProject());
			patterns.createControl(groupPatterns);
		} else {
			Label label = new Label(groupPatterns, SWT.NONE);
			label
					.setText(PreferenceMessages.TclActiveStateDebuggerBlock_patternsInProjectProperties);
			label.setEnabled(false);
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		Group options = SWTFactory.createGroup(composite,
				PreferenceMessages.instrumentation_options, 2, 1,
				GridData.FILL_HORIZONTAL);
		final GridLayout optionsLayout = (GridLayout) options.getLayout();
		optionsLayout.marginHeight = 0;
		optionsLayout.makeColumnsEqualWidth = true;
		// error action
		Composite compositeErrorAction = SWTFactory.createComposite(options,
				options.getFont(), 2, 2, 0);
		final GridLayout errorActionLayout = (GridLayout) compositeErrorAction
				.getLayout();
		errorActionLayout.marginHeight = 0;
		errorActionLayout.marginWidth = 0;
		SWTFactory.createLabel(compositeErrorAction,
				PreferenceMessages.instrumentation_errorAction_label, 1);
		errorActionCombo = SWTFactory.createCombo(compositeErrorAction,
				SWT.READ_ONLY, 1, createErrorActionItems());
		List<InstrumentationFeature> left = new ArrayList<InstrumentationFeature>();
		left.add(InstrumentationFeature.DYNPROC);
		left.add(InstrumentationFeature.AUTOLOAD);
		final List<InstrumentationFeature> right = new ArrayList<InstrumentationFeature>(
				Arrays.asList(InstrumentationFeature.values()));
		right.removeAll(left);
		instrumentationButtons = new ArrayList<Button>(left.size()
				+ right.size());
		createButtons(instrumentationButtons, left, SWTFactory.createComposite(
				options, options.getFont(), 1, 1,
				GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
		createButtons(instrumentationButtons, right, SWTFactory
				.createComposite(options, options.getFont(), 1, 1,
						GridData.FILL_HORIZONTAL));
		return composite;
	}

	/**
	 * @param buttons
	 * @param features
	 * @param parent
	 */
	private void createButtons(List<Button> buttons,
			List<InstrumentationFeature> features, Composite parent) {
		final GridLayout layout = (GridLayout) parent.getLayout();
		layout.marginHeight = 0;
		layout.verticalSpacing = 2;
		layout.marginBottom = 5;
		layout.marginWidth = 0;
		for (InstrumentationFeature feature : features) {
			final Button button = SWTFactory.createCheckButton(parent, feature
					.getCaption());
			button.setData(FEATURE_KEY, feature);
			buttons.add(button);
		}
	}

	private static final String FEATURE_KEY = TclActiveStateDebuggerPreferencePage.class
			.getName()
			+ "#FEATURE"; //$NON-NLS-1$

	private void initValues() {
		// patterns
		if (patterns != null) {
			patterns
					.setValue(PatternListIO
							.decode(getString(TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_PATTERNS)));
		}
		// instrumentation features
		Set<InstrumentationFeature> result = InstrumentationFeature
				.decode(getString(TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_FEATURES));
		for (final Button button : instrumentationButtons) {
			InstrumentationFeature feature = (InstrumentationFeature) button
					.getData(FEATURE_KEY);
			button.setSelection(result.contains(feature));
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
		// spawnpoints
		spawnpointBlock.initValues();
	}

	@Override
	protected void initialize() {
		super.initialize();
		initValues();
	}

	@Override
	public void performDefaults() {
		super.performDefaults();
		initValues();
	}

	protected void createEngineBlock(Composite parent) {
		super.createEngineBlock(parent);
		createPDXGroup(parent);
	}

	protected void createOtherBlock(Composite parent) {
		addDownloadLink(parent, PreferenceMessages.DebuggingEngineDownloadPage,
				PreferenceMessages.DebuggingEngineDownloadPageLink);
	}

	private TclLoggingPathBlock loggingBlock;

	@Override
	protected void createLoggingBlock(Composite parent) {
		final Group group = SWTFactory.createGroup(parent,
				ScriptDebugPreferencesMessages.LoggingGroupLabel, 3, 1,
				GridData.FILL_BOTH);
		loggingBlock = new TclLoggingPathBlock();
		loggingBlock.createControl(group, getRelevantEnvironments());
		Map<?, ?> paths = EnvironmentPathUtils
				.decodePaths(getString(getLogFileNamePreferenceKey()));
		loggingBlock.setPaths(paths);
		loggingBlock
				.setEnableLogging(TclActiveStateDebuggerEnvironment
						.decodeBooleans(getString(TclActiveStateDebuggerPreferencePage.LOG_ENABLE)));
	}

	@Override
	protected void saveLoggingOptions() {
		String loggingPaths = EnvironmentPathUtils.encodePaths(loggingBlock
				.getPaths());
		setString(getLogFileNamePreferenceKey(), loggingPaths);
		setString(TclActiveStateDebuggerPreferencePage.LOG_ENABLE,
				TclActiveStateDebuggerEnvironment.encodeBooleans(loggingBlock
						.getEnableLogging()));
	}

	private void createPDXGroup(final Composite parent) {

		final Group group = SWTFactory.createGroup(parent,
				PreferenceMessages.DebuggingEnginePDXGroup, 3, 1,
				GridData.FILL_BOTH);
		pdxPath = new EnvironmentPathBlock(true);
		pdxPath.createControl(group, getRelevantEnvironments());
		Map<?, ?> paths = EnvironmentPathUtils
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
		if (patterns != null) {
			setString(
					TclActiveStateDebuggerPreferencePage.INSTRUMENTATION_PATTERNS,
					PatternListIO.encode(patterns.getValue()));
		}
		// Instrumentation features
		Set<InstrumentationFeature> selectedFeatures = new HashSet<InstrumentationFeature>();
		for (final Button button : instrumentationButtons) {
			final InstrumentationFeature feature = (InstrumentationFeature) button
					.getData(FEATURE_KEY);
			if (button.getSelection()) {
				selectedFeatures.add(feature);
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

	@SuppressWarnings("unchecked")
	@Override
	protected IPreferenceChangeRebuildPrompt getPreferenceChangeRebuildPrompt(
			boolean workspaceSettings, Collection changedOptions) {
		if (changedOptions
				.contains(TclActiveStateDebuggerPreferencePage.PREF_SPAWNPOINTS)) {
			return PreferenceChangeRebuildPrompt
					.create(
							workspaceSettings,
							PreferenceMessages.TclActiveStateDebuggerBlock_rebuildTitle,
							PreferenceMessages.TclActiveStateDebuggerBlock_rebuildMessage);
		} else {
			return null;
		}
	}

}
