/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation
 *******************************************************************************/
package org.eclipse.dltk.ui.tests.swtbot.operations;

import java.util.List;

import net.sf.swtbot.SWTBotTestCase;
import net.sf.swtbot.eclipse.finder.SWTEclipseBot;
import net.sf.swtbot.eclipse.finder.widgets.SWTBotView;
import net.sf.swtbot.finder.ControlFinder;
import net.sf.swtbot.matcher.WidgetOfType;
import net.sf.swtbot.utils.SWTUtils;
import net.sf.swtbot.wait.Conditions;
import net.sf.swtbot.wait.DefaultCondition;
import net.sf.swtbot.widgets.SWTBotCheckBox;
import net.sf.swtbot.widgets.SWTBotRadio;
import net.sf.swtbot.widgets.SWTBotShell;
import net.sf.swtbot.widgets.SWTBotText;
import net.sf.swtbot.widgets.SWTBotTree;
import net.sf.swtbot.widgets.SWTBotTreeItem;
import net.sf.swtbot.widgets.TimeoutException;
import net.sf.swtbot.widgets.WidgetNotFoundException;

import org.eclipse.dltk.internal.ui.wizards.NewWizardMessages;
import org.eclipse.dltk.ui.tests.swtbot.ErrorMessages;
import org.eclipse.dltk.ui.tests.swtbot.complex.ProjectTests;
import org.eclipse.swt.widgets.Button;

public class ProjectOperations extends Operations {

	private static final String DLG_CREATE_TCL_PROJECT = "Create Tcl project";

	private static final String RADIO_FROM_EXIST = createLabel(NewWizardMessages.ScriptProjectWizardFirstPage_LocationGroup_external_desc);
	private static final String RADIO_IN_WORKSPACE = createLabel(NewWizardMessages.ScriptProjectWizardFirstPage_LocationGroup_workspace_desc);
	private static final String RADIO_USE_ALT_INTERPR = createLabel(NewWizardMessages.ScriptProjectWizardFirstPage_InterpreterEnvironmentGroup_specific_compliance);
	// String hardcode in TclProjectWizardFirstPage class
	private static final String CHECK_DELETE_CONTENTS = createLabel(OperationMessages.ProjectOperations_check_delete_contents);

	public ProjectOperations(SWTEclipseBot bot) {
		super(bot);
	}

	/**
	 * Create a Tcl project in workspace with default interpreter with use main
	 * menu and switch to Tcl perspective. (DLTK-502)
	 * 
	 * @see ProjectTests#testCreateTclProject001()
	 * 
	 * @param name
	 * @param promptPerspective
	 */
	public void createPromptPerspective(final String name) {
		try {
			getBot().menu(MENU_FILE).menu(MENU_NEW).menu(MENU_NEW_PROJECT)
					.click();
			getBot().waitUntil(Conditions.shellIsActive(DLG_NEW_PROJECT));
			expandTree("Tcl", "Tcl Project");
			getBot().button(WIZARD_NEXT).click();
			getBot()
					.waitUntil(Conditions.shellIsActive(DLG_CREATE_TCL_PROJECT));
			getBot().textWithLabel("Project name:").setText(name);
			getBot().button(WIZARD_FINISH).click();
			getBot().waitUntil(
					Conditions.shellIsActive("Open Associated Perspective?"));
			SWTBotShell shell = getBot().shell("Open Associated Perspective?");
			getBot().button("Yes").click();
			getBot().waitUntil(Conditions.shellCloses(shell));
			getProjectItem(name);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Create a Tcl project in workspace with default interpreter with use
	 * context menu of Script Explorer view. (DLTK-746)
	 * 
	 * @see ProjectTests#testCreateTclProject002()
	 * 
	 * @param name
	 */
	public void createFromScriptExplorer(final String name) {
		try {
			SWTBotShell shell = openInterpreters();
			getBot().button("OK").click();
			getBot().waitUntil(Conditions.shellCloses(shell));

			SWTBotView viewBot = getBot().view(VIEW_SCRIPT_EXPLORER);
			viewBot.setFocus();
			final SWTBotTree treeBot = getBot().tree();
			clickContextSubMenu(treeBot, MENU_NEW, MENU_TCL_PROJECT);
			getBot()
					.waitUntil(Conditions.shellIsActive(DLG_CREATE_TCL_PROJECT));
			internalCreate(name);

			getProjectItem(name);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	public void createFromSource(final String name,
			final String externalProjectName, boolean useDetectPackage) {
		try {
			openNewTclProject();
			getBot().radio(RADIO_FROM_EXIST).click();

			SWTBotText srcDir = getBot().textWithLabel("Directory:");
			SWTBotCheckBox detectPackage = getBot().checkBox(
					OperationMessages.ProjectOperations_check_delete_package);

			SWTBotTestCase.assertEnabled(srcDir);
			SWTBotTestCase.assertEnabled(detectPackage);

			srcDir.setText(externalProjectName);

			String errMessage = null;
			if (useDetectPackage) {
				errMessage = ErrorMessages.Common_errNotChecked;
			} else {
				errMessage = ErrorMessages.Common_errChecked;
				detectPackage.click();
			}

			errMessage = ErrorMessages.format(errMessage,
					OperationMessages.ProjectOperations_check_delete_package);

			SWTBotTestCase.assertEquals(errMessage, detectPackage.isChecked(),
					useDetectPackage);

			internalCreate(name);

			SWTBotTreeItem projectBot = getProjectItem(name);
			checkExternalProject(projectBot);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Create a Tcl project using a project specific interpreter. (DLTK-505)
	 * 
	 * @see ProjectTests#testCreateTclProject005()
	 * 
	 * @param name
	 */
	public void createWithAltInterpr(final String name) {
		try {
			SWTBotShell shell = openInterpreters();
			getBot().button("OK").click();
			getBot().waitUntil(Conditions.shellCloses(shell));

			openNewTclProject();
			getBot().radio(RADIO_USE_ALT_INTERPR).click();
			getBot().comboBox(1).setSelection(1);
			internalCreate(name);

			getProjectItem(name);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	public void create(final String name) {
		try {
			SWTBotShell shell = openInterpreters();
			getBot().button("OK").click();
			getBot().waitUntil(Conditions.shellCloses(shell));

			openNewTclProject();
			internalCreate(name);

			getProjectItem(name);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	protected void internalCreate(final String name)
			throws WidgetNotFoundException, TimeoutException {
		SWTBotShell shell = getBot().shell(DLG_CREATE_TCL_PROJECT);
		getBot().textWithLabel("Project name:").setText(name);
		getBot().button(WIZARD_FINISH).click();
		getBot().waitUntil(Conditions.shellCloses(shell));
	}

	/**
	 * Delete Tcl project. (DLTK-509)
	 * 
	 * @see ProjectTests#testDeleteProject()
	 * 
	 * @param name
	 */
	public void deleteProject(final String name) {
		try {
			getProjectItem(name);
			getBot().menu(MENU_EDIT).menu(MENU_DELETE).click();
			getBot()
					.waitUntil(
							Conditions
									.shellIsActive(OperationMessages.Operations_dlg_delete_resource));
			SWTBotShell shell = getBot().shell(
					OperationMessages.Operations_dlg_delete_resource);
			getBot().button("OK").click();
			getBot().waitUntil(Conditions.shellCloses(shell));

			assertProjectNotExist(name);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Delete a Tcl project without project content deletion (DLTK-626).<br>
	 * Delete a Tcl project with project content (DLTK-627).
	 * 
	 * @see ProjectTests#testDeleteProjectByContextMenu001
	 * @see ProjectTests#testDeleteProjectByContextMenu002
	 * 
	 * @param name
	 * @param removeContent
	 */
	public void deleteProjectByContextMenu(String name, boolean removeContent) {
		try {
			SWTBotTreeItem projectBot = getProjectItem(name);
			projectBot.contextMenu(MENU_DELETE).click();
			getBot()
					.waitUntil(
							Conditions
									.shellIsActive(OperationMessages.Operations_dlg_delete_resource));
			SWTBotShell shell = getBot().shell(
					OperationMessages.Operations_dlg_delete_resource);

			List findControls = new ControlFinder()
					.findControls(new WidgetOfType<Button>(Button.class));
			if (SWTUtils.getText(findControls.get(0)).equals(
					OperationMessages.ProjectOperations_check_delete_contents)) {
				// If Eclipse 3.4
				SWTBotCheckBox removeContentBot = getBot().checkBox(
						CHECK_DELETE_CONTENTS);
				if (removeContent) {
					removeContentBot.click();
					assertChecked(removeContentBot);
				} else {
					assertNotChecked(removeContentBot);
				}
			} else {
				// If Eclipse 3.3
				if (removeContent) {
					SWTBotRadio removeContentBot = new SWTBotRadio(
							(Button) findControls.get(0));
					removeContentBot.click();
					assertSelected(removeContentBot);
				} else {
					SWTBotRadio removeContentBot = getBot()
							.radio(
									createLabel(OperationMessages.ProjectOperations_radio_do_not_delete_contents));
					removeContentBot.click();
					assertSelected(removeContentBot);
				}
			}

			getBot().button("OK").click();
			getBot().waitUntil(Conditions.shellCloses(shell));//
			assertProjectNotExist(name);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Open a Tcl project. (DLTK-508)
	 * 
	 * @see ProjectTests#testOpenTclProject()
	 * 
	 * @param name
	 */
	public void openProject(final String name) {
		try {
			final SWTBotTreeItem projectItem = getProjectItem(name);
			projectItem.contextMenu(SCRIPT_POPUP_OPEN_PRJ).click();

			getBot().waitUntil(new DefaultCondition() {
				public String getFailureMessage() {
					return ErrorMessages.Project_errOpen;
				}

				public boolean test() throws Exception {
					try {
						return isOpenProject(projectItem);
					} catch (Exception ex) {
						return false;
					}
				}
			});
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Close a Tcl project. (DLTK-507)
	 * 
	 * @see ProjectTests#testCloseTclProject()
	 * 
	 * @param name
	 */
	public void closeProject(final String name) {
		try {
			final SWTBotTreeItem projectItem = getProjectItem(name);
			projectItem.contextMenu(SCRIPT_POPUP_CLOSE_PRJ).click();

			getBot().waitUntil(new DefaultCondition() {
				public String getFailureMessage() {
					return ErrorMessages.Project_errClose;
				}

				public boolean test() throws Exception {
					try {
						return !isOpenProject(projectItem);
					} catch (Exception ex) {
						return true;
					}
				}
			});
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	protected SWTBotShell openNewTclProject() throws WidgetNotFoundException,
			TimeoutException {
		getBot().menu(MENU_FILE).menu(MENU_NEW).menu(MENU_TCL_PROJECT).click();
		getBot().waitUntil(Conditions.shellIsActive(DLG_CREATE_TCL_PROJECT));
		return getBot().shell(DLG_CREATE_TCL_PROJECT);
	}

	// /////////////////////////////////////////////////////////////////////////
	//
	// Check methods
	//
	// /////////////////////////////////////////////////////////////////////////
	protected boolean isOpenProject(SWTBotTreeItem projectItem)
			throws WidgetNotFoundException, TimeoutException {
		// TODO
		return projectItem.contextMenu(SCRIPT_POPUP_CLOSE_PRJ) != null;
	}

	protected void checkExternalProject(SWTBotTreeItem projectBot)
			throws WidgetNotFoundException, TimeoutException {
		// TODO: add implementation
	}

	// /////////////////////////////////////////////////////////////////////////
	//
	// Asserts
	//
	// /////////////////////////////////////////////////////////////////////////
	protected void assertProjectNotExist(String name) {
		try {
			getProjectItem(name);
			SWTBotTestCase.fail(ErrorMessages.Project_errDelete);
		} catch (WidgetNotFoundException exExpected) {
		} catch (TimeoutException exExpected) {
		}
	}
}
