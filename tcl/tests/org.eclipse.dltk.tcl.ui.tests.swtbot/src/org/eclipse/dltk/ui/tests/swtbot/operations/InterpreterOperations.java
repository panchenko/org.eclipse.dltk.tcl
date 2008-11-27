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

import java.text.MessageFormat;

import net.sf.swtbot.SWTBotTestCase;
import net.sf.swtbot.eclipse.finder.SWTEclipseBot;
import net.sf.swtbot.wait.Conditions;
import net.sf.swtbot.widgets.SWTBotButton;
import net.sf.swtbot.widgets.SWTBotCombo;
import net.sf.swtbot.widgets.SWTBotRadio;
import net.sf.swtbot.widgets.SWTBotShell;
import net.sf.swtbot.widgets.SWTBotTable;
import net.sf.swtbot.widgets.SWTBotTree;
import net.sf.swtbot.widgets.SWTBotTreeItem;
import net.sf.swtbot.widgets.TimeoutException;
import net.sf.swtbot.widgets.WidgetNotFoundException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersMessages;
import org.eclipse.dltk.ui.tests.swtbot.DltkTestsHelper;
import org.eclipse.dltk.ui.tests.swtbot.ErrorMessages;
import org.eclipse.dltk.ui.tests.swtbot.complex.InterpreterTests;

public class InterpreterOperations extends Operations {

	private static final String TCL_INTERPRETER_TYPE = OperationMessages.InterpreterOperations_tcl_interpreter_type;

	private static final String COL_NAME = OperationMessages.InterpreterOperations_col_name;
	private static final String COL_TYPE = OperationMessages.InterpreterOperations_col_type;
	private static final String COL_LOCATION = OperationMessages.InterpreterOperations_col_location;

	private static final String BTN_ADD = OperationMessages.InterpreterOperations_btn_add;

	private static final String FLD_INTERPR_NAME = createLabel(InterpretersMessages.addInterpreterDialog_InterpreterEnvironmentName);
	private static final String FLD_INTERPR_TYPE = createLabel(InterpretersMessages.addInterpreterDialog_InterpreterEnvironmentType);
	private static final String FLD_INTERPR_PATH = createLabel(InterpretersMessages.addInterpreterDialog_InterpreterExecutableName);

	private static final String DLG_ADD_INTERPRETER = OperationMessages.InterpreterOperations_dlg_add_interpreter;
	private static final String DLG_ADD_LIBRARY = OperationMessages.InterpreterOperations_dlg_add_library;
	private static final String DLG_EDIT_LIBRARY = OperationMessages.InterpreterOperations_dlg_edit_library;
	private static final String TAB_LIBRARIES = OperationMessages.InterpreterOperations_tab_libraries;

	private static final String RADIO_ALT_INTERPRETER = createLabel(InterpretersMessages.InterpretersComboBlock_1);
	private static final String RADIO_DEF_INTERPRETER = "Workspace default interpreter ({0})"; //$NON-NLS-1$
	private static final String TREE_INTERPRETER_LIBS = "Libraries and external folders on the build path:"; //$NON-NLS-1$

	private static final String BTN_OK = "OK";
	private static final String BTN_CANCEL = "Cancel";
	private static final String BTN_REMOVE = "Remove";

	public InterpreterOperations(SWTEclipseBot bot) {
		super(bot);
	}

	/**
	 * Create an interpreter using add button. (DLTK-510)
	 * 
	 * @see InterpreterTests#testCreate()
	 * 
	 * @param path
	 * @param interprName
	 * @param isDefault
	 * @param index
	 */
	public void createInterpreter(String path, String interprName,
			boolean isDefault, int index) {
		try {
			openInterpreters();

			getBot().button(BTN_ADD).click();
			getBot().waitUntil(Conditions.shellIsActive(DLG_ADD_INTERPRETER));
			getBot().textWithLabel(FLD_INTERPR_NAME).setText(interprName);
			getBot().textWithLabel(FLD_INTERPR_PATH).setText(path);

			String type = getBot().comboBoxWithLabel(FLD_INTERPR_TYPE)
					.getText();
			SWTBotTestCase.assertEquals(TCL_INTERPRETER_TYPE, type);

			waitEnableAndClick(BTN_OK);
			getBot().waitUntil(Conditions.shellIsActive(DLG_PREFERENCES));
			SWTBotShell shell = getBot().shell(DLG_PREFERENCES);
			SWTBotTable tableBot = getBot().table();
			getBot().waitUntil(Conditions.tableHasRows(tableBot, index + 1));

			// Alternative interpreter will add to first row because table is
			// sorting by name.
			checkInterpreter(tableBot, path, interprName, isDefault, 0);

			waitEnableAndClick(BTN_OK);
			getBot().waitUntil(Conditions.shellCloses(shell));
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Change default interpreter. (DLTK-602)
	 * 
	 * @see InterpreterTests#testChangeDefaultInterpreter()
	 * 
	 * @throws WidgetNotFoundException
	 * @throws TimeoutException
	 */
	public void changeDefaultInterpreter() {
		try {
			openInterpreters();
			SWTBotShell shell = getBot().shell(DLG_PREFERENCES);
			SWTBotTable tableBot = getBot().table();

			checkInterpreter(tableBot, DltkTestsHelper.ALT_INTERPRETER_PATH,
					DltkTestsHelper.ALT_INTERPRETER_ID, false, 0);
			checkInterpreter(tableBot, DltkTestsHelper.DEF_INTERPRETER_PATH,
					DltkTestsHelper.DEF_INTERPRETER_ID, true, 1);

			tableBot.getTableItem(0).check();

			checkInterpreter(tableBot, DltkTestsHelper.ALT_INTERPRETER_PATH,
					DltkTestsHelper.ALT_INTERPRETER_ID, true, 0);
			checkInterpreter(tableBot, DltkTestsHelper.DEF_INTERPRETER_PATH,
					DltkTestsHelper.DEF_INTERPRETER_ID, false, 1);

			waitEnableAndClick(BTN_OK);
			getBot().waitUntil(Conditions.shellCloses(shell));
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Remove default interpreter. (DLTK-635)
	 * 
	 * @see InterpreterTests#testRemoveDefaultInterpreter()
	 * 
	 * @throws WidgetNotFoundException
	 * @throws TimeoutException
	 */
	public void removeDefaultInterpreter() {
		try {
			openInterpreters();
			SWTBotShell shell = getBot().shell(DLG_PREFERENCES);
			SWTBotTable tableBot = getBot().table();

			checkInterpreter(tableBot, DltkTestsHelper.ALT_INTERPRETER_PATH,
					DltkTestsHelper.ALT_INTERPRETER_ID, false, 0);
			checkInterpreter(tableBot, DltkTestsHelper.DEF_INTERPRETER_PATH,
					DltkTestsHelper.DEF_INTERPRETER_ID, true, 1);

			tableBot.select(1);
			getBot().button(BTN_REMOVE).click();

			tableBot = getBot().table();
			getBot().waitUntil(Conditions.tableHasRows(tableBot, 1));

			checkInterpreter(tableBot, DltkTestsHelper.ALT_INTERPRETER_PATH,
					DltkTestsHelper.ALT_INTERPRETER_ID, true, 0);

			waitEnableAndClick(BTN_OK);
			getBot().waitUntil(Conditions.shellCloses(shell));
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Remove several interpreters. (DLTK-644)
	 * 
	 * @see InterpreterTests#testRemoveInterpreters()
	 * 
	 * @throws WidgetNotFoundException
	 * @throws TimeoutException
	 */
	public void removeInterpreters() {
		try {
			openInterpreters();
			SWTBotShell shell = getBot().shell(DLG_PREFERENCES);
			SWTBotTable tableBot = getBot().table();

			getBot().waitUntil(Conditions.tableHasRows(tableBot, 3));

			tableBot.select(new int[] { 0, 2 });
			getBot().button(BTN_REMOVE).click();

			tableBot = getBot().table();
			getBot().waitUntil(Conditions.tableHasRows(tableBot, 1));

			waitEnableAndClick(BTN_OK);
			getBot().waitUntil(Conditions.shellCloses(shell));
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Remove all interpreters. (DLTK-645)
	 * 
	 * @see InterpreterTests#testRemoveAllInterpreters()
	 * 
	 * @throws WidgetNotFoundException
	 * @throws TimeoutException
	 */
	public void removeAllInterpreters() {
		try {
			openInterpreters();
			SWTBotShell shell = getBot().shell(DLG_PREFERENCES);
			SWTBotTable tableBot = getBot().table();

			getBot().waitUntil(Conditions.tableHasRows(tableBot, 1));

			tableBot.select(0);
			getBot().button(BTN_REMOVE).click();

			tableBot = getBot().table();
			getBot().waitUntil(Conditions.tableHasRows(tableBot, 0));

			waitEnableAndClick(BTN_OK);
			getBot().waitUntil(Conditions.shellCloses(shell));
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Add interpreter to a project with use Script Explorer context menu
	 * "Properties". (DLTK-512)
	 * 
	 * @param projectName
	 * 
	 * @see InterpreterTests#testAdd001()
	 * 
	 * @throws WidgetNotFoundException
	 * @throws TimeoutException
	 */
	public void addInterpreter001(String projectName) {
		try {
			SWTBotShell shell = openLibraryTab(projectName);

			SWTBotTree librariesBot = getBot().treeWithLabel(
					TREE_INTERPRETER_LIBS);
			String errorMessage = ErrorMessages.Interpreter_errInterprLibFound;
			SWTBotTestCase.assertFalse(errorMessage, librariesBot.hasItems());

			shell = openAddInterpreterLibrary();
			selectAltInterpreterLibrary();
			getBot().waitUntil(
					Conditions.shellIsActive(DLG_PRJ_PROPERTIES + projectName));
			shell = getBot().shell(DLG_PRJ_PROPERTIES + projectName);
			checkInterprLib(librariesBot, DltkTestsHelper.ALT_INTERPRETER_ID);

			waitEnableAndClick(BTN_OK);
			getBot().waitUntil(Conditions.shellCloses(shell));

			checkInterprContainer(projectName,
					DltkTestsHelper.ALT_INTERPRETER_ID);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Add interpreter to a project with use Script Explorer context menu
	 * "Build Path/Configure Build Path...". (DLTK-512)
	 * 
	 * @param projectName
	 * 
	 * @see InterpreterTests#testAdd002()
	 * 
	 * @throws WidgetNotFoundException
	 * @throws TimeoutException
	 */
	public void addInterpreter002(String projectName) {
		try {
			SWTBotTreeItem projectBot = getProjectItem(projectName);
			projectBot
					.contextMenu(
							OperationMessages.InterpreterOperations_context_menu_build_path)
					.menu(
							OperationMessages.InterpreterOperations_menu_configure_build_path)
					.click(); //$NON-NLS-1$
			// clickContextSubMenu(projectBot,
			// OperationMessages.InterpreterOperations_context_menu_build_path,
			//OperationMessages.InterpreterOperations_menu_configure_build_path)
			// ;
			final String dlgProp = DLG_PRJ_PROPERTIES + projectName;
			getBot().waitUntil(Conditions.shellIsActive(dlgProp));
			SWTBotShell shell = getBot().shell(dlgProp);
			getBot().tabItem(TAB_LIBRARIES).activate(); //$NON-NLS-1$

			SWTBotTree librariesBot = getBot().treeWithLabel(
					TREE_INTERPRETER_LIBS);
			String errorMessage = ErrorMessages.Interpreter_errInterprLibFound;
			SWTBotTestCase.assertFalse(errorMessage, librariesBot.hasItems());

			shell = openAddInterpreterLibrary();
			selectDefInterpreterLibrary(false);
			getBot().waitUntil(Conditions.shellIsActive(dlgProp));
			shell = getBot().shell(dlgProp);
			checkInterprLib(librariesBot, DltkTestsHelper.DEF_INTERPRETER_ID);

			waitEnableAndClick(BTN_OK);
			getBot().waitUntil(Conditions.shellCloses(shell));

			checkInterprContainer(projectName,
					DltkTestsHelper.DEF_INTERPRETER_ID);

		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Try to add several Interpreter Libraries to a project. (DLTK-646)
	 * 
	 * @param projectName
	 * 
	 * @see InterpreterTests#testAdd003()
	 * 
	 * @throws WidgetNotFoundException
	 * @throws TimeoutException
	 */
	public void addInterpreter003(String projectName) {
		try {
			SWTBotShell shell = openLibraryTab(projectName);

			SWTBotTree librariesBot = getBot().treeWithLabel(
					TREE_INTERPRETER_LIBS);
			checkInterprLib(librariesBot, DltkTestsHelper.DEF_INTERPRETER_ID);

			shell = openAddInterpreterLibrary();
			selectAltInterpreterLibrary();

			getBot().waitUntil(
					Conditions.shellIsActive(DLG_PRJ_PROPERTIES + projectName));
			shell = getBot().shell(DLG_PRJ_PROPERTIES + projectName);
			// TODO: add check of error message

			checkInterprLib(librariesBot, DltkTestsHelper.DEF_INTERPRETER_ID);

			String errorMessage = ErrorMessages.Interpreter_errSeveralInterprLibFound;
			assertInterprLibNotExist(errorMessage, librariesBot,
					DltkTestsHelper.ALT_INTERPRETER_ID);

			final SWTBotButton btnBot = getBot().button(BTN_OK);
			assertNotEnabled(btnBot);

			// check for error message
			try {
				getBot()
						.text(
								OperationMessages.InterpreterOperations_build_path_can_not_contain_mult_interpr);
			} catch (WidgetNotFoundException ex) {
				SWTBotTestCase
						.assertTrue(
								"Error message \'"
										+ OperationMessages.InterpreterOperations_build_path_can_not_contain_mult_interpr
										+ "\' not found.", false);
			}
			waitEnableAndClick(BTN_CANCEL);
			getBot().waitUntil(Conditions.shellCloses(shell));

			checkInterprContainer(projectName,
					DltkTestsHelper.DEF_INTERPRETER_ID);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Set default interpreter to a project. (DLTK-513)
	 * 
	 * @param projectName
	 * 
	 * @see InterpreterTests#testSetDefault()
	 * 
	 * @throws WidgetNotFoundException
	 * @throws TimeoutException
	 */
	public void changeInterpreter001(String projectName) {
		try {
			SWTBotShell shell = openLibraryTab(projectName);

			SWTBotTree librariesBot = getBot().treeWithLabel(
					TREE_INTERPRETER_LIBS);
			checkInterprLib(librariesBot, DltkTestsHelper.ALT_INTERPRETER_ID);

			shell = openEditInterpreterLibrary(librariesBot);
			selectDefInterpreterLibrary(true);

			getBot().waitUntil(
					Conditions.shellIsActive(DLG_PRJ_PROPERTIES + projectName));
			shell = getBot().shell(DLG_PRJ_PROPERTIES + projectName);
			checkInterprLib(librariesBot, DltkTestsHelper.DEF_INTERPRETER_ID);

			waitEnableAndClick(BTN_OK);
			getBot().waitUntil(Conditions.shellCloses(shell));

			checkInterprContainer(projectName,
					DltkTestsHelper.DEF_INTERPRETER_ID);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Set alternative interpreter to a project. (DLTK-514)
	 * 
	 * @param projectName
	 * 
	 * @see InterpreterTests#testSetAlternative()
	 * 
	 * @throws WidgetNotFoundException
	 * @throws TimeoutException
	 */
	public void changeInterpreter002(String projectName) {
		try {
			SWTBotShell shell = openLibraryTab(projectName);

			SWTBotTree librariesBot = getBot().treeWithLabel(
					TREE_INTERPRETER_LIBS);
			checkInterprLib(librariesBot, DltkTestsHelper.DEF_INTERPRETER_ID);

			shell = openEditInterpreterLibrary(librariesBot);
			selectAltInterpreterLibrary();

			getBot().waitUntil(
					Conditions.shellIsActive(DLG_PRJ_PROPERTIES + projectName));
			shell = getBot().shell(DLG_PRJ_PROPERTIES + projectName);
			checkInterprLib(librariesBot, DltkTestsHelper.ALT_INTERPRETER_ID);

			waitEnableAndClick(BTN_OK);
			getBot().waitUntil(Conditions.shellCloses(shell));

			checkInterprContainer(projectName,
					DltkTestsHelper.ALT_INTERPRETER_ID);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	/**
	 * Remove interpreter from a project. (DLTK-515)
	 * 
	 * @param projectName
	 * 
	 * @see InterpreterTests#testRemoveFromProject()
	 * 
	 * @throws WidgetNotFoundException
	 * @throws TimeoutException
	 */
	public void removeInterpreter(String projectName) {
		try {
			SWTBotShell shell = openLibraryTab(projectName);

			SWTBotTree librariesBot = getBot().treeWithLabel(
					TREE_INTERPRETER_LIBS);
			checkInterprLib(librariesBot, DltkTestsHelper.DEF_INTERPRETER_ID);

			// librariesBot.select(0);
			librariesBot.getAllItems()[0].select();
			getBot().button(BTN_REMOVE).click();
			String errorMessage = ErrorMessages.Interpreter_errInterprLibFound;
			SWTBotTestCase.assertFalse(errorMessage, librariesBot.hasItems());

			waitEnableAndClick(BTN_OK);
			getBot().waitUntil(Conditions.shellCloses(shell));

			SWTBotTreeItem projectBot = getProjectItem(projectName);
			String nodeText = createInterpLibNodeName(DltkTestsHelper.DEF_INTERPRETER_ID);
			// TODO
			// SWTBotTestCase.assertNull(
			// ErrorMessages.Interpreter_errInterprLibFound, projectBot
			// .expand().getNode(nodeText));
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	protected SWTBotShell openLibraryTab(String projectName)
			throws WidgetNotFoundException, TimeoutException {
		SWTBotTreeItem projectItem = getProjectItem(projectName);
		SWTBotShell shell = openProperties(projectItem);
		expandTree("Tcl", "Build Path"); //$NON-NLS-1$ //$NON-NLS-2$
		getBot().tabItem(TAB_LIBRARIES).activate();
		return shell;
	}

	protected SWTBotShell openAddInterpreterLibrary()
			throws WidgetNotFoundException, TimeoutException {
		getBot()
				.button(OperationMessages.InterpreterOperations_btn_add_library)
				.click();
		getBot().waitUntil(Conditions.shellIsActive(DLG_ADD_LIBRARY));
		SWTBotShell shell = getBot().shell(DLG_ADD_LIBRARY);
		getBot().list().select("Tcl Interpreter Libraries"); //$NON-NLS-1$
		getBot().button(WIZARD_NEXT).click();
		return shell;
	}

	protected SWTBotShell openEditInterpreterLibrary(SWTBotTree librariesBot)
			throws WidgetNotFoundException, TimeoutException {
		librariesBot.getAllItems()[0].select();
		// librariesBot.select(0);
		getBot().button(OperationMessages.InterpreterOperations_btn_edit)
				.click();
		getBot().waitUntil(Conditions.shellIsActive(DLG_EDIT_LIBRARY));
		return getBot().shell(DLG_EDIT_LIBRARY);
	}

	protected void selectAltInterpreterLibrary()
			throws WidgetNotFoundException, TimeoutException {
		SWTBotCombo altInterprBot = getBot().comboBox(0);
		SWTBotTestCase.assertNotEnabled(altInterprBot);

		getBot().radio(RADIO_ALT_INTERPRETER).click();
		SWTBotTestCase.assertEnabled(altInterprBot);

		altInterprBot.setSelection(DltkTestsHelper.ALT_INTERPRETER_ID);
		getBot().button(WIZARD_FINISH).click();
	}

	protected void selectDefInterpreterLibrary(boolean select)
			throws WidgetNotFoundException, TimeoutException {
		String widgetName = MessageFormat.format(RADIO_DEF_INTERPRETER,
				new Object[] { DltkTestsHelper.DEF_INTERPRETER_ID });
		SWTBotRadio defInterprBot = getBot().radio(widgetName);
		boolean isDefInterpreterSelected = defInterprBot.isSelected();

		if (select) {
			defInterprBot.click();
			isDefInterpreterSelected = defInterprBot.isSelected();
		}

		SWTBotTestCase.assertTrue(isDefInterpreterSelected);
		SWTBotTestCase.assertNotEnabled(getBot().comboBox(0));
		getBot().button(WIZARD_FINISH).click();
	}

	private String createInterpLibNodeName(String interprName) {
		return "Interpreter Libraries [" + interprName + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void checkInterprContainer(String projectName, String interprName)
			throws WidgetNotFoundException, TimeoutException {
		getProjectItem(projectName);
		String nodeText = createInterpLibNodeName(interprName);
		expandTree(projectName, nodeText);
	}

	protected void checkInterpreter(SWTBotTable tableBot, String path,
			String interprName, boolean isDefault, int index) {

		SWTBotTestCase.assertTrue(ErrorMessages.Common_errColumnCount, tableBot
				.columnCount() == 3);
		SWTBotTestCase
				.assertEquals(interprName, tableBot.cell(index, COL_NAME));
		SWTBotTestCase.assertEquals(TCL_INTERPRETER_TYPE, tableBot.cell(index,
				COL_TYPE));
		IPath expectedPath = new Path(path);
		IPath actualPath = new Path(tableBot.cell(index, COL_LOCATION));
		SWTBotTestCase.assertEquals(expectedPath, actualPath);

		String errorMessage = isDefault ? ErrorMessages.Common_errNotChecked
				: ErrorMessages.Common_errChecked;
		errorMessage = ErrorMessages.format(errorMessage, "Row " + index); //$NON-NLS-1$
		SWTBotTestCase.assertEquals(errorMessage, tableBot.getTableItem(index)
				.isChecked(), isDefault);
	}

	protected void checkInterprLib(SWTBotTree librariesBot, String interprName)
			throws WidgetNotFoundException {
		librariesBot.getTreeItem(createInterpLibNodeName(interprName));
	}

	protected void assertInterprLibNotExist(String errorMessage,
			SWTBotTree librariesBot, String interprName) {
		try {
			checkInterprLib(librariesBot, interprName);
		} catch (Exception e) {
			SWTBotTestCase.fail(errorMessage);
		}
	}
}
