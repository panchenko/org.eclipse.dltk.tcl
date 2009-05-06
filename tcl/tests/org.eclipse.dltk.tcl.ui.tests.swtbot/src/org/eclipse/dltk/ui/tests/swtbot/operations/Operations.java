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
import java.util.StringTokenizer;

import org.eclipse.dltk.ui.tests.swtbot.ErrorMessages;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.eclipse.finder.SWTEclipseBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.MenuFinder;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRadio;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;

public class Operations {

	public static final String MENU_FILE = OperationMessages.Operations_menu_file;
	public static final String MENU_EDIT = OperationMessages.Operations_menu_edit;
	public static final String MENU_REFACTOR = OperationMessages.Operations_menu_refactor;
	public static final String MENU_NAVIGATE = OperationMessages.Operations_menu_navigate;
	public static final String MENU_WINDOW = OperationMessages.Operations_menu_window;
	public static final String MENU_PREFERENCES = OperationMessages.Operations_menu_preferences;
	public static final String MENU_NEW = OperationMessages.Operations_menu_new;
	public static final String MENU_OPEN = OperationMessages.Operations_menu_open;
	public static final String MENU_NEW_PROJECT = OperationMessages.Operations_menu_new_project;
	public static final String MENU_TCL_PROJECT = OperationMessages.Operations_menu_tcl_project;
	public static final String MENU_NEW_FILE = OperationMessages.Operations_menu_new_file;
	public static final String MENU_TCL_FILE = OperationMessages.Operations_menu_tcl_file;
	public static final String MENU_NEW_FOLDER = OperationMessages.Operations_menu_new_folder;
	public static final String MENU_NEW_SOURCE_FOLDER = OperationMessages.Operations_menu_new_source_folder;
	public static final String MENU_COPY = OperationMessages.Operations_menu_copy;
	public static final String MENU_PASTE = OperationMessages.Operations_menu_paste;
	public static final String MENU_DELETE = OperationMessages.Operations_menu_delete;
	public static final String MENU_RENAME = OperationMessages.Operations_menu_rename;

	public static final String MENU_RUN_AS = OperationMessages.Operations_menu_run_as;
	public static final String MENU_DEBUG_AS = OperationMessages.Operations_menu_debug_as;

	public static final String MENU_OPEN_DECLARATION = OperationMessages.Operations_menu_open_declaration;

	public static final String WIZARD_NEXT = createLabel(IDialogConstants.NEXT_LABEL);
	public static final String WIZARD_FINISH = createLabel(IDialogConstants.FINISH_LABEL);

	public static final String SCRIPT_POPUP_OPEN_PRJ = OperationMessages.Operations_script_popup_open_prj;
	public static final String SCRIPT_POPUP_CLOSE_PRJ = OperationMessages.Operations_script_popup_close_prj;
	public static final String SCRIPT_POPUP_PROPERTIES = OperationMessages.Operations_script_popup_properties;

	public static final String DLG_PREFERENCES = OperationMessages.Operations_dlg_preferences;
	public static final String DLG_PRJ_PROPERTIES = OperationMessages.Operations_dlg_prj_properties;
	public static final String DLG_NEW_PROJECT = OperationMessages.Operations_dlg_new_project;
	public static final String DLG_NEW_FILE = OperationMessages.Operations_dlg_new_file;
	public static final String DLG_NEW_FOLDER = OperationMessages.Operations_dlg_new_folder;
	public static final String DLG_NEW_SOURCE_FOLDER = OperationMessages.Operations_dlg_new_source_folder;
	public static final String DLG_CREATE_TCL_FILE = OperationMessages.Operations_dlg_create_tcl_file;
	public static final String DLG_RENAME = OperationMessages.Operations_dlg_rename;
	public static final String DLG_RENAME_SCRIPT_PROJECT = OperationMessages.Operations_dlg_rename_script_project;
	public static final String DLG_RENAME_SOURCE_FOLDER = OperationMessages.Operations_dlg_rename_source_folder;
	public static final String DLG_RENAME_SOURCE_MODULE = OperationMessages.Operations_dlg_rename_source_module;
	public static final String DLG_CONFIRM_DELETE = OperationMessages.Operations_dlg_confirm_delete;

	public static final String VIEW_SCRIPT_EXPLORER = OperationMessages.Operations_view_script_explorer;
	public static final String VIEW_CONSOLE = OperationMessages.Operations_view_console;

	public static final String PREFERENCES_TCL = OperationMessages.Operations_preferences_tcl;

	/**
	 * Remove <code>&</code> symbol.
	 * 
	 * @param src
	 * @return
	 */
	public static String createLabel(String src) {
		return src.replace("&", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private SWTEclipseBot bot;

	public Operations(SWTEclipseBot bot) {
		this.bot = bot;
	}

	protected SWTEclipseBot getBot() {
		return bot;
	}

	protected void waitEnableAndClick(final String name)
			throws WidgetNotFoundException, TimeoutException {
		final SWTBotButton btnBot = getBot().button(name);
		getBot().waitUntil(new DefaultCondition() {
			private String errorMessage = "Can't click to button " + name; //$NON-NLS-1$

			public String getFailureMessage() {
				return errorMessage;
			}

			public boolean test() throws Exception {
				try {
					btnBot.click();
					return true;
				} catch (Exception ex) {
					errorMessage = ex.getLocalizedMessage();
					return false;
				}
			}
		});
	}

	protected SWTBotShell openPreferences() throws WidgetNotFoundException,
			TimeoutException {
		getBot().menu(MENU_WINDOW).menu(MENU_PREFERENCES).click();
		getBot().waitUntil(Conditions.shellIsActive(DLG_PREFERENCES));
		SWTBotShell result = getBot().shell(DLG_PREFERENCES);
		return result;
	}

	protected SWTBotShell openInterpreters() throws WidgetNotFoundException,
			TimeoutException {
		SWTBotShell shell = openPreferences();
		expandTree(PREFERENCES_TCL, "Interpreters"); //$NON-NLS-1$
		return shell;
	}

	protected SWTBotShell openProperties(SWTBotTreeItem projectTree)
			throws WidgetNotFoundException, TimeoutException {
		projectTree.contextMenu(SCRIPT_POPUP_PROPERTIES).click();
		String dlgProp = DLG_PRJ_PROPERTIES + projectTree.getText();
		getBot().waitUntil(Conditions.shellIsActive(dlgProp));
		return getBot().shell(dlgProp);
	}

	protected SWTBotTreeItem expandTree(String item, String subitem)
			throws WidgetNotFoundException, TimeoutException {
		SWTBotTree treeBot = getBot().tree();
		SWTBotTreeItem result = treeBot.getTreeItem(item).expand().expandNode(
				subitem);
		result.select();
		return result;
	}

	protected SWTBotStyledText getConsole() throws WidgetNotFoundException,
			TimeoutException {
		SWTBotView viewBot = getBot().view(VIEW_CONSOLE);
		viewBot.setFocus();
		return getBot().styledText(0);
	}

	protected void closeConsole() throws WidgetNotFoundException,
			TimeoutException {
		SWTBotView viewBot = getBot().view(VIEW_CONSOLE);
		viewBot.close();
	}

	protected SWTBotTreeItem getProjectItem(String name)
			throws WidgetNotFoundException, TimeoutException {
		SWTBotView viewBot = getBot().view(VIEW_SCRIPT_EXPLORER);
		viewBot.setFocus();
		final SWTBotTree treeBot = getBot().tree();
		SWTBotTreeItem result = treeBot.getTreeItem(name);
		result.select();
		return result;
	}

	protected SWTBotTreeItem navigateToProjectElement(String projectName,
			String pathToElement) throws WidgetNotFoundException,
			TimeoutException {
		return navigateToProjectElement(getProjectItem(projectName),
				pathToElement);
	}

	protected SWTBotTreeItem navigateToProjectElement(SWTBotTreeItem item,
			String pathToElement) throws WidgetNotFoundException,
			TimeoutException {
		StringTokenizer tok = new StringTokenizer(pathToElement, "/\\"); //$NON-NLS-1$
		while (tok.hasMoreTokens()) {
			item.expand();
			String name = tok.nextToken();
			item = item.getNode(name);
			if (item == null) {
				throw new WidgetNotFoundException("Element " + name //$NON-NLS-1$
						+ " not found in project tree"); //$NON-NLS-1$
			}
		}
		item.select();
		return item;
	}
	
	////////////////////////////////////////////////////////////////////////////
	//
	// Asserts
	//
	////////////////////////////////////////////////////////////////////////////

	protected void assertNotEnabled(AbstractSWTBot widget) {
		String errMessage = ErrorMessages.Common_errEnabled;
		errMessage = ErrorMessages.format(errMessage, widget.getText());
		SWTBotTestCase.assertFalse(errMessage, widget.isEnabled());
	}

	protected void assertSelected(SWTBotRadio radio) {
		String errMessage = ErrorMessages.Common_errNotChecked;
		errMessage = ErrorMessages.format(errMessage, radio.getText());
		SWTBotTestCase.assertTrue(errMessage, radio.isSelected());
	}

	protected void assertNotSelected(SWTBotRadio radio) {
		String errMessage = ErrorMessages.Common_errNotChecked;
		errMessage = ErrorMessages.format(errMessage, radio.getText());
		SWTBotTestCase.assertFalse(errMessage, radio.isSelected());
	}

	protected void assertChecked(SWTBotCheckBox detectPackage) {
		String errMessage = ErrorMessages.Common_errNotChecked;
		errMessage = ErrorMessages.format(errMessage, detectPackage.getText());
		SWTBotTestCase.assertTrue(errMessage, detectPackage.isChecked());
	}

	protected void assertNotChecked(SWTBotCheckBox detectPackage) {
		String errMessage = ErrorMessages.Common_errChecked;
		errMessage = ErrorMessages.format(errMessage, detectPackage.getText());
		SWTBotTestCase.assertFalse(errMessage, detectPackage.isChecked());
	}

	public void assertProjectElementExist(String projectName,
			String pathToElement) {
		try {
			navigateToProjectElement(projectName, pathToElement);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}
	}

	public void assertProjectElementNotExist(String projectName, String path) {
		try {
			navigateToProjectElement(projectName, path);
			SWTBotTestCase.fail("Unexpected element in a tree: " + path); //$NON-NLS-1$
		} catch (WidgetNotFoundException ex) {
		} catch (TimeoutException ex) {
		}
	}

	public void assertProjectElementNotExist(String projectName, String path,
			String name) {
		SWTBotTreeItem bot = null;
		try {
			bot = navigateToProjectElement(projectName, path);
		} catch (WidgetNotFoundException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		} catch (TimeoutException ex) {
			SWTBotTestCase.fail(ex.getLocalizedMessage());
		}

		bot.expand();

		String errorMessage = "Unexpected element in a tree. Path: " + path //$NON-NLS-1$
				+ ", element: " + name; //$NON-NLS-1$
		SWTBotTestCase.assertNotNull(errorMessage, bot.getNode(name));
	}

	protected void clickContextSubMenu(SWTBotTreeItem treeItem,
			final String menuName, final String subMenuName) {
		treeItem.contextMenu(menuName).setFocus();
		clickContextSubMenu(menuName, subMenuName);
	}

	protected void clickContextSubMenu(SWTBotTree tree, final String menuName,
			final String subMenuName) {
		tree.contextMenu(menuName).click();
		clickContextSubMenu(menuName, subMenuName);
	}

	private void clickContextSubMenu(final String menuName,
			final String subMenuName) {
		MenuItem menuItem = UIThreadRunnable.syncExec(Display.getDefault(),
				new WidgetResult<MenuItem>() {
					public MenuItem run() {
						List<MenuItem> menus = new MenuFinder()
								.findMenus(WidgetMatcherFactory
										.withMnemonic(subMenuName));
						for (MenuItem item : menus) {
							try {
								String name = item.getParent().getParentItem()
										.getText().replace("&", "");
								if (name.startsWith(menuName)) {
									return item;
								}
							} catch (Exception e) {
								// do nothing
							}
						}
						return null;
					}
				});
		if (null == menuItem) {
			throw new WidgetNotFoundException(
					"Can not found widget with name: " + subMenuName);
		}
		new SWTBotMenu(menuItem).click();
	}

	// protected void printShell() {
	// try {
	// SWTBotShell[] shells = getBot().shells();
	// int shelCount = shells.length;
	// System.out.println("shelCount: " + shelCount);
	// for (int index = 0; index < shelCount; index++) {
	// System.out.println("shell: " + shells[index].getText()
	// + ", op: " + shells[index].isOpen() + ", vis: "
	// + shells[index].isVisible() + ", en: "
	// + shells[index].isEnabled());
	// }
	// System.out.println("Active: " + getBot().activeShell().getText());
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }
}
