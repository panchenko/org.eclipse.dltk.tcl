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
package org.eclipse.dltk.tcl.internal.ui.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.internal.core.ProjectRefreshOperation;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.dltk.tcl.core.TclCorePreferences;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

@SuppressWarnings("restriction")
public class TclCorePreferencePage extends
		AbstractConfigurationBlockPropertyAndPreferencePage {

	private static int IDX_ADD = 0;
	private static int IDX_EDIT = 1;
	private static int IDX_REMOVE = 2;

	protected static class TclCorePreferenceBlock extends AbstractOptionsBlock {

		private class TclCheckContentAdapter implements IListAdapter,
				IDialogFieldListener {

			private Set<String> contributedElements = null;
			private final PreferenceKey listKey;
			private final char itemSeparator;

			public TclCheckContentAdapter(PreferenceKey listKey,
					char listSeparator) {
				this.listKey = listKey;
				this.itemSeparator = listSeparator;
			}

			public void customButtonPressed(ListDialogField field, int index) {
				String edited = null;
				if (index != IDX_ADD) {
					edited = (String) field.getSelectedElements().get(0);
				}
				if (index == IDX_ADD || index == IDX_EDIT) {
					TclCheckContentExcludeInputDialog dialog = new TclCheckContentExcludeInputDialog(
							getShell(), edited, field.getElements());
					if (dialog.open() == Window.OK) {
						if (edited != null) {
							field.replaceElement(edited, dialog.getResult());
						} else {
							field.addElement(dialog.getResult());
						}
					}
				}
			}

			private boolean canEdit(List<?> selectedElements) {
				return selectedElements.size() == 1
						&& (contributedElements == null || !contributedElements
								.containsAll(selectedElements));
			}

			public void doubleClicked(ListDialogField field) {
				if (canEdit(field.getSelectedElements())) {
					customButtonPressed(field, IDX_EDIT);
				}
			}

			public void selectionChanged(ListDialogField field) {
				List<?> selectedElements = field.getSelectedElements();
				field.enableButton(IDX_EDIT, canEdit(selectedElements));
			}

			public void dialogFieldChanged(DialogField field) {
				savePatterns((ListDialogField) field);
			}

			public void loadPatterns(ListDialogField field) {
				final List<String> elements = new ArrayList<String>();
				if (contributedElements != null) {
					elements.addAll(contributedElements);
				}
				final String[] patterns = TextUtils.split(getString(listKey),
						itemSeparator);
				if (patterns != null) {
					elements.addAll(Arrays.asList(patterns));
				}
				field.setElements(elements);
				selectionChanged(field);
			}

			private void savePatterns(ListDialogField field) {
				final List<?> elements = field.getElements();
				if (contributedElements != null) {
					elements.removeAll(contributedElements);
				}
				setString(listKey, TextUtils.join(elements, itemSeparator));
			}

			public void setContributedElements(Set<String> value) {
				this.contributedElements = value;
			}

		}

		private static class AssociationViwerSorter extends ViewerSorter {

			private final Set<String> highlighted;

			public AssociationViwerSorter(Set<String> highlighted) {
				this.highlighted = highlighted;
			}

			@Override
			public int category(Object element) {
				return highlighted.contains(element) ? 0 : 1;
			}

		}

		private static class AssociationLabelProvider extends LabelProvider
				implements IFontProvider {

			private final Set<String> highlighted;

			public AssociationLabelProvider(Set<String> highlighted) {
				this.highlighted = highlighted;
			}

			public Font getFont(Object element) {
				if (highlighted.contains(element)) {
					return JFaceResources.getFontRegistry().getBold(
							JFaceResources.DIALOG_FONT);
				}
				return null;
			}
		}

		@Override
		protected void initialize() {
			super.initialize();
			includeAdapter.loadPatterns(includeDialog);
			excludeAdapter.loadPatterns(excludeDialog);
		}

		@Override
		public void performDefaults() {
			super.performDefaults();
			includeAdapter.loadPatterns(includeDialog);
			excludeAdapter.loadPatterns(excludeDialog);
		}

		@Override
		protected boolean saveValues() {
			final boolean value = super.saveValues();
			/*
			 * TODO start job only if associations changed. Listen for
			 * preference change instead?
			 */
			new Job(TclPreferencesMessages.TclCorePreferencePage_0) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						final IScriptProject[] projects = DLTKCore.create(
								ResourcesPlugin.getWorkspace().getRoot())
								.getScriptProjects(TclNature.NATURE_ID);
						ResourcesPlugin.getWorkspace().run(
								new ProjectRefreshOperation(projects), monitor);
					} catch (CoreException e) {
						DLTKCore.error(TclPreferencesMessages.TclCorePreferencePage_1, e);
					}
					return Status.OK_STATUS;
				}
			}.schedule(500);
			return value;
		}

		private static final PreferenceKey[] KEYS = new PreferenceKey[] {
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_EMPTY_EXTENSION_LOCAL),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_EMPTY_EXTENSION_REMOTE),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_ANY_EXTENSION_LOCAL),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_ANY_EXTENSION_REMOTE),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_EXCLUDES),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						DLTKCore.LANGUAGE_FILENAME_ASSOCIATIONS) };

		/**
		 * @param context
		 * @param project
		 * @param allKeys
		 * @param container
		 */
		public TclCorePreferenceBlock(IStatusChangeListener context,
				IProject project, IWorkbenchPreferenceContainer container) {
			super(context, project, KEYS, container);
			excludeAdapter = new TclCheckContentAdapter(KEYS[4],
					TclCorePreferences.CHECK_CONTENT_EXCLUDE_SEPARATOR);
			includeAdapter = new TclCheckContentAdapter(KEYS[5],
					DLTKCore.LANGUAGE_FILENAME_ASSOCIATION_SEPARATOR);
		}

		/**
		 * @since 2.0
		 */
		@Override
		protected void validateValuePresenceFor(PreferenceKey key) {
			if (TclPlugin.PLUGIN_ID.equals(key.getQualifier())
					&& DLTKCore.LANGUAGE_FILENAME_ASSOCIATIONS.equals(key
							.getName())) {
				return;
			}
			super.validateValuePresenceFor(key);
		}

		private void createCheckbox(Composite block, String label,
				PreferenceKey key) {
			final Button checkButton = SWTFactory.createCheckButton(block,
					label);
			GridData data = new GridData();
			data.horizontalIndent = 16;
			checkButton.setLayoutData(data);
			bindControl(checkButton, key, null);
		}

		private final TclCheckContentAdapter excludeAdapter;
		private ListDialogField excludeDialog;

		private final TclCheckContentAdapter includeAdapter;
		private ListDialogField includeDialog;

		@Override
		protected Control createOptionsBlock(Composite parent) {
			Composite block = SWTFactory.createComposite(parent, parent
					.getFont(), 1, 1, GridData.FILL_BOTH);
			SWTFactory
					.createLabel(
							block,
							TclPreferencesMessages.TclCorePreferencePage_checkContentWithoutExtension,
							1);
			createCheckbox(block,
					TclPreferencesMessages.TclCorePreferencePage_local, KEYS[0]);
			createCheckbox(block,
					TclPreferencesMessages.TclCorePreferencePage_remote,
					KEYS[1]);
			SWTFactory
					.createLabel(
							block,
							TclPreferencesMessages.TclCorePreferencePage_checkContentAnyExtension,
							1);
			createCheckbox(block,
					TclPreferencesMessages.TclCorePreferencePage_local, KEYS[2]);
			createCheckbox(block,
					TclPreferencesMessages.TclCorePreferencePage_remote,
					KEYS[3]);

			final Composite patternComposite = SWTFactory.createComposite(
					block, block.getFont(), 1, 1, GridData.FILL_BOTH);
			final GridLayout patternLayout = new GridLayout();
			patternLayout.numColumns = 2;
			patternLayout.marginHeight = 0;
			patternLayout.marginWidth = 0;
			patternComposite.setLayout(patternLayout);

			final String[] buttons = new String[] {
					TclPreferencesMessages.TclCorePreferencePage_checkContentAddExclude,
					TclPreferencesMessages.TclCorePreferencePage_checkContentEditExclude,
					TclPreferencesMessages.TclCorePreferencePage_checkContentRemoveExclude };
			{
				SWTFactory
						.createLabel(
								patternComposite,
								TclPreferencesMessages.TclCorePreferencePage_Associations,
								2);
				final Set<String> associations = DLTKLanguageManager
						.loadFilenameAssociations(TclNature.NATURE_ID);
				includeAdapter.setContributedElements(associations);
				includeDialog = new ListDialogField(includeAdapter, buttons,
						new AssociationLabelProvider(associations)) {
					@Override
					protected boolean canRemove(ISelection selection) {
						if (selection instanceof IStructuredSelection) {
							if (includeAdapter.contributedElements != null) {
								for (Iterator<?> i = ((IStructuredSelection) selection)
										.iterator(); i.hasNext();) {
									final Object element = i.next();
									if (includeAdapter.contributedElements
											.contains(element)) {
										return false;
									}
								}
							}
							return true;
						}
						return false;
					}
				};
				includeDialog.setDialogFieldListener(includeAdapter);
				includeDialog.setRemoveButtonIndex(IDX_REMOVE);

				includeDialog.setViewerSorter(new AssociationViwerSorter(
						associations));
				final Control listControl = includeDialog
						.getListControl(patternComposite);
				final GridData listControlLayoutData = new GridData(
						GridData.FILL_BOTH);
				listControlLayoutData.heightHint = new PixelConverter(
						listControl).convertHeightInCharsToPixels(6);
				listControl.setLayoutData(listControlLayoutData);
				includeDialog.getButtonBox(patternComposite).setLayoutData(
						new GridData(GridData.HORIZONTAL_ALIGN_FILL
								| GridData.VERTICAL_ALIGN_BEGINNING));
			}
			{
				SWTFactory
						.createLabel(
								patternComposite,
								TclPreferencesMessages.TclCorePreferencePage_checkContentExcludes,
								2);
				excludeDialog = new ListDialogField(excludeAdapter, buttons,
						new LabelProvider());
				excludeDialog.setDialogFieldListener(excludeAdapter);
				excludeDialog.setRemoveButtonIndex(IDX_REMOVE);

				excludeDialog.setViewerSorter(new ViewerSorter());
				final Control listControl = excludeDialog
						.getListControl(patternComposite);
				final GridData listControlLayoutData = new GridData(
						GridData.FILL_BOTH);
				listControlLayoutData.heightHint = new PixelConverter(
						listControl).convertHeightInCharsToPixels(6);
				listControl.setLayoutData(listControlLayoutData);
				excludeDialog.getButtonBox(patternComposite).setLayoutData(
						new GridData(GridData.HORIZONTAL_ALIGN_FILL
								| GridData.VERTICAL_ALIGN_BEGINNING));
			}
			return block;
		}
	}

	@Override
	protected AbstractOptionsBlock createOptionsBlock(
			IStatusChangeListener newStatusChangedListener, IProject project,
			IWorkbenchPreferenceContainer container) {
		return new TclCorePreferenceBlock(newStatusChangedListener, project,
				container);
	}

	protected String getHelpId() {
		return null;
	}

	protected String getProjectHelpId() {
		return null;
	}

	@Override
	protected String getNatureId() {
		return TclNature.NATURE_ID;
	}

	protected void setDescription() {
		// empty
	}

	protected void setPreferenceStore() {
		// empty
	}

	protected String getPreferencePageId() {
		return null;
	}

	protected String getPropertyPageId() {
		return null;
	}

}
